package studentvote;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/*
 * Main function
 */
@SpringBootApplication
public class Application {
	
	public static void main(String[] args) {
		try {
			StudentController.initDBConnection();
		} catch (Exception e) {
			System.out.println("Unable to initialize db connection: " + e.getMessage());
			e.printStackTrace();
			System.exit(1);
		} 
		
		System.out.println("Successfully established database connection");
        SpringApplication.run(Application.class, args);
    }

}
