package studentvote;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.*;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/*
 * Author Radhika
 * Connects to database and APIs 
 */
@RestController
public class StudentController {

	static Connection connection = null;

	/*
	 * Fetches JDBC connection, username and password from a file
	 */
	static String[] getUserNamePassword() throws FileNotFoundException {
		InputStream in = StudentController.class.getResourceAsStream("/static/database.txt");
		if ( in == null ) {
			throw new FileNotFoundException("file not found: ");
		} else {
			Scanner s = new Scanner(in).useDelimiter("\\A");
			String result = s.hasNext() ? s.next() : "";
			return result.split(" ");
		}
	}
	
	/*
	 * Initiates the Database connection
	 */

	public static void initDBConnection() throws ClassNotFoundException, SQLException, FileNotFoundException {
		String[] creds = getUserNamePassword();
		Class.forName("com.mysql.jdbc.Driver");
		connection = DriverManager.getConnection(creds[0].trim(), creds[1].trim(), creds[2].trim());
	}

	/*
	 * Post method to store student information in database
	 * Returns the stored student information
	 */
	@RequestMapping(value = "/vote", method = RequestMethod.POST)
	public Student doPost(@RequestBody Student student) throws ServerException, FileNotFoundException{
		String name = student.getName();
		String fruit = student.getFruit();

		if(name.isEmpty() || fruit.isEmpty()){
			throw new InvalidParameterException();
		}

		if(name.length() > 100){
			throw new InvalidParameterException();
		}

		try{
			String query = "Insert into StudentSelection(StudentName, Fruit) values (?,?) ON DUPLICATE KEY UPDATE StudentName = ?, Fruit = ?;";
			String queryFriuts = "select exists(select * from Fruits where fruit = ?);";
			PreparedStatement statement  = connection.prepareStatement(queryFriuts);
			statement.setString(1, fruit);
			ResultSet rs = statement.executeQuery();
			if(rs.next() && rs.getBoolean(1)) {
				statement = null;
				statement  = connection.prepareStatement(query);
				statement.setString(1, name);
				statement.setString(2, fruit);
				statement.setString(3, name);
				statement.setString(4, fruit);
				statement.execute();
				return student;
			} else {
				throw new InvalidParameterException();
			}

		} catch (SQLIntegrityConstraintViolationException ee) {
			return student;
		} catch (SQLException ee) {
			throw new InvalidParameterException(ee);
		} 
	}

	/*
	 * Get method to Fetch the total votes for each fruit
	 * Returns a list of vote objects
	 */
	@RequestMapping(value = "/votes")
	public List<Vote> getVotes() throws  ServerException, FileNotFoundException{
		List<Vote> votes = new ArrayList<Vote>();
		try {
			String query = "select f.Fruit, IF(sd.votes IS NULL, 0, sd.votes) as Votes from Fruits f left join (select Fruit, count(Fruit) as votes from StudentSelection group by Fruit) as sd on f.fruit = sd.fruit order by 2 desc;";
			PreparedStatement statement = connection.prepareStatement(query);
			ResultSet rs = statement.executeQuery();
			while(rs.next()){
				String fruit = rs.getString(1);
				Integer count = rs.getInt(2);
				votes.add(new Vote(fruit, count));
			}
			return votes;
		} catch (SQLException ee) {
			throw new InvalidParameterException(ee);
		} 
	}
	
	/*
	 * Get method to fetch all kinds of fruits available in database
	 * returns a list of fruits
	 */

	@RequestMapping(value = "/fruits")
	public List<String> populateFruits() throws ServerException, FileNotFoundException{
		List<String> fruits = new ArrayList<String>();
		try{
			String query = "select f.Fruit, IF(sd.votes IS NULL, 0, sd.votes) as Votes from Fruits f left join (select Fruit, count(Fruit) as votes from StudentSelection group by Fruit) as sd on f.fruit = sd.fruit order by 2 desc;";
			PreparedStatement statement = connection.prepareStatement(query);
			ResultSet rs = statement.executeQuery();
			while(rs.next()){
				fruits.add(rs.getString(1));
			}
			return fruits;
		} catch (SQLException ee) {
			throw new InvalidParameterException(ee);
		}
	}

	/*
	 * Get method to fetch all available students in database
	 * returns a list of students
	 */
	@RequestMapping(value = "/students")
	public List<String> populateStudents() throws ServerException, FileNotFoundException{
		List<String> students = new ArrayList<String>();
		try{
			String query = "select * from Student;";
			PreparedStatement statement = connection.prepareStatement(query);
			ResultSet rs = statement.executeQuery();
			while(rs.next()){
				students.add(rs.getString(1));
			}
			return students;
		} catch (SQLException ee) {
			throw new InvalidParameterException(ee);
		}
	}

	/*
	 * Get method to fetch the vote of a particular student
	 * Returns the fruit voted by the student
	 */
	@RequestMapping(value = "/vote")
	public String fetchTheVote(@RequestParam(value="name") String studentName) throws FileNotFoundException, ServerException{
		try {
			String query = "select * from StudentSelection where StudentName = ?;";
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setString(1, studentName);
			ResultSet rs = statement.executeQuery();
			if(rs.next()){
				String fruit = rs.getString(2);
				return fruit;
			} else {
				throw new InvalidParameterException();
			}
		} catch (SQLException ee) {
			throw new InvalidParameterException(ee);
		} 
	}
	
}
