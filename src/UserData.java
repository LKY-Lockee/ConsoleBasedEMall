import java.math.BigDecimal;
import java.util.ArrayList;

public class UserData {
	private String userName;
	private boolean isAdmin;
	private ArrayList<ProductData> bought = new ArrayList<>();

	public UserData(String userName, boolean isAdmin) {
		this.userName = userName;
		this.isAdmin = isAdmin;
	}

	public String getUserName() {
		return userName;
	}

	public boolean isAdmin() {
		return isAdmin;
	}

	public ArrayList<ProductData> getBought() {
		return bought;
	}

	public void addBought(ProductData product) {
		bought.add(product);
	}

	public BigDecimal getTotalPrice() {
		BigDecimal totalPrice = new BigDecimal(0);
		for (ProductData product : bought) {
			totalPrice = totalPrice.add(product.price.multiply(new BigDecimal(product.num)));
		}
		return totalPrice;
	}
}
