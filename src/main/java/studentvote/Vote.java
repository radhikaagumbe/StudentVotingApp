package studentvote;

/*
 * Vote class containing a fruit and its total number of votes
 */

public class Vote {
	String fruit;
	Integer votes;

	/*
	 * Constructor
	 */
	
	public Vote() {
	}

	/*
	 * Constructor
	 */
	
	public Vote(String fruit, Integer votes) {
		this.fruit = fruit;
		this.votes = votes;
	}
	
	/*
	 * Fruit getter
	 * returns the fruit
	 */
	public String getFruit() {
		return fruit;
	}
	
	/*
	 * Votes getter
	 * returns the number of votes
	 */
	
	public Integer getVotes() {
		return votes;
	}
}
