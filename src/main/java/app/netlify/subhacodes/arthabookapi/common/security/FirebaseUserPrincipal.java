package app.netlify.subhacodes.arthabookapi.common.security;

public class FirebaseUserPrincipal {
    private final String uid;
    private final String email;
    private final boolean emailVerified;

    public FirebaseUserPrincipal(
            String uid,
            String email,
            boolean emailVerified
    ) {
        this.uid = uid;
        this.email = email;
        this.emailVerified = emailVerified;
    }

    public String getUid() {
        return uid;
    }

    public String getEmail() {
        return email;
    }

    public boolean isEmailVerified() {
        return emailVerified;
    }
}
