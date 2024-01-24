package gameOfLife;

public class Job {
	public String jobTitle;
	public int salary;
	public int tax;
	
	public Job(String title, int salary, int tax) {
		jobTitle = title;
		this.salary = salary;
		this.tax = tax;
	}
	
	public String toString() {
		return (jobTitle+":\nSalary = "+ salary + "K\nTaxes = "+tax+"K");
	}
	
}
