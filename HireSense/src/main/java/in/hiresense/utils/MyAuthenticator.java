package in.hiresense.utils;

import jakarta.mail.Authenticator;
import jakarta.mail.PasswordAuthentication;

public class MyAuthenticator extends Authenticator{

	private static final String EMAIL = "demomailing192@gmail.com";
    private static final String PASSWORD = "mgkw yzni necr qkaa";

    @Override
    protected PasswordAuthentication getPasswordAuthentication() {
        PasswordAuthentication pwdAuth=new PasswordAuthentication(EMAIL,PASSWORD);
        return pwdAuth;

    }

}
