package com.groupe3.api.utils;

import org.jose4j.jwa.AlgorithmConstraints;
import org.jose4j.jwk.RsaJsonWebKey;
import org.jose4j.jwk.RsaJwkGenerator;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.jose4j.lang.JoseException;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Classe qui s'occupe de vérifier l'intégrité des mots de passes
 * ainsi que la génération et validation de JSONWEBTOKENS
 * @Author Romain CHAHINE <romain.chahine@outlook.fr>
 */
@Component
public class Security {

    private RsaJsonWebKey rsaJsonWebKey;

    public Security() {
        try {
            this.rsaJsonWebKey = RsaJwkGenerator.generateJwk(4096);
        } catch(JoseException e) {
            System.exit(1);
        }
    }
    /**
     * Vérifie que le mdp est conforme au hash enregistré
     * @param plainPassword le mdp clair
     * @param hashedPassword le mdp hashé
     * @see BCrypt
     * @return vrai si conforme
     */
    public static boolean checkPassword(final String plainPassword, final String hashedPassword) {
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }

    /**
     * Renvoi le hash d'un mot de passe en clair
     * @param plainPassword le mdp clair
     * @see BCrypt
     * @return le hash correspondant
     */
    public static String hashPassword(final String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt());
    }

    /**
     * Génère un JWT(jsonwebtoken) avec
     * @param body
     * @see RsaJsonWebKey
     * @see RsaJwkGenerator
     * @see JwtClaims
     * @see JsonWebSignature
     * @throws JoseException
     * @return le JWT généré
     */
    public String generateJWT(final HashMap<String, Object> body) throws JoseException {

        // Create the Claims, which will be the content of the JWT
        JwtClaims claims = new JwtClaims();
        claims.setExpirationTimeMinutesInTheFuture(60); // time when the token will expire (10 minutes from now)
        claims.setGeneratedJwtId(); // a unique identifier for the token
        claims.setIssuedAtToNow();  // when the token was issued/created (now)
        claims.setSubject("subject"); // the subject/principal is whom the token is about

        for(Map.Entry me: body.entrySet()) {
            claims.setClaim(me.getKey().toString(), me.getValue());
        }
        // A JWT is a JWS and/or a JWE with JSON claims as the payload.
        // In this example it is a JWS so we create a JsonWebSignature object.
        JsonWebSignature jws = new JsonWebSignature();

        // The payload of the JWS is JSON content of the JWT Claims
        jws.setPayload(claims.toJson());

        // The JWT is signed using the private key
        jws.setKey(this.rsaJsonWebKey.getPrivateKey());

        // Set the Key ID (kid) header because it's just the polite thing to do.
        // We only have one key in this example but a using a Key ID helps
        // facilitate a smooth key rollover process
        jws.setKeyIdHeaderValue(this.rsaJsonWebKey.getKeyId());

        // Set the signature algorithm on the JWT/JWS that will integrity protect the claims
        jws.setAlgorithmHeaderValue(AlgorithmIdentifiers.RSA_USING_SHA256);

        // Sign the JWS and produce the compact serialization or the complete JWT/JWS
        // representation, which is a string consisting of three dot ('.') separated
        // base64url-encoded parts in the form Header.Payload.Signature
        // If you wanted to encrypt it, you can simply set this jwt as the payload
        // of a JsonWebEncryption object and set the cty (Content Type) header to "jwt".
        return jws.getCompactSerialization();
    }

    public Map<String, Object> readJWT(final String jwt) throws InvalidJwtException {
        JwtConsumer jwtConsumer = new JwtConsumerBuilder()
            .setRequireExpirationTime() // the JWT must have an expiration time
            .setAllowedClockSkewInSeconds(30) // allow some leeway in validating time based claims to account for clock skew
            .setRequireSubject() // the JWT must have a subject claim
            .setExpectedIssuer("Issuer") // whom the JWT needs to have been issued by
            .setExpectedAudience("Audience") // to whom the JWT is intended for
            .setVerificationKey(this.rsaJsonWebKey.getKey()) // verify the signature with the public key
            .setJwsAlgorithmConstraints( // only allow the expected signature algorithm(s) in the given context
                    AlgorithmConstraints.ConstraintType.PERMIT, AlgorithmIdentifiers.RSA_USING_SHA256) // which is only RS256 here
            .build(); // create the JwtConsumer instance

        //  Validate the JWT and process it to the Claims
        JwtClaims jwtClaims = jwtConsumer.processToClaims(jwt);
        return jwtClaims.getClaimsMap();


    }
}
