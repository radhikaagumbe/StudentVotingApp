package studentvote;

/*
 * Student class containing name of the student and the fruit selection
 */

public class Student {

	String name;
	String fruit;
	
	/*
	 * Constructor
	 */
	
	public Student(){
	}
	
	/*
	 * Constructor
	 */
	
	public Student(String a, String b){
		this.name = a;
		this.fruit = b;
	}
	
	/*
	 * Name getter
	 * returns the name of a student
	 */
	
	public String getName(){
		return name;
	}
	
	/*
	 * Fruit getter
	 * returns the fruit
	 */
	
	public String getFruit(){
		return fruit;
	}
}
