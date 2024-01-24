package gameOfLife;

public class House {
	public String houseName;
	public int buyingPrice;
	public int sellingPrice;
	
	public House(String Name, int bPrice, int sPrice) {
		houseName = Name;
		buyingPrice = bPrice;
		sellingPrice = sPrice;
	}
	
	public String tostring() {
		return houseName +"\nPrice = " + buyingPrice + "K$\nSelling Price = " + sellingPrice+"K$";
	}
}
