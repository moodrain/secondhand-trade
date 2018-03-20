package muyu.trade;

import muyu.trade.util.HttpClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashMap;

@SpringBootApplication
public class TradeApplication {

	public static void main(String[] args) throws Exception
	{
		SpringApplication.run(TradeApplication.class, args);
	}
}