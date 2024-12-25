import java.math.BigDecimal;

public class ProductData {
	public final int id;
	public final String name;
	public final BigDecimal price;
	public final int num;

	public ProductData(int id, String name, BigDecimal price, int num) {
		this.id = id;
		this.name = name;
		this.price = price;
		this.num = num;
	}
}
