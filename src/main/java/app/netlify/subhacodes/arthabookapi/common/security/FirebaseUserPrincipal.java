package app.netlify.subhacodes.arthabookapi.common.security;

public record FirebaseUserPrincipal(String uid, String email, boolean emailVerified) {
}
