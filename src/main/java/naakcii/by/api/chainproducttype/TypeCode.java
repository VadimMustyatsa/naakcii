package naakcii.by.api.chainproducttype;

public enum TypeCode {
	DISCOUNT("Скидка", "", "discount"),
	ONE_PLUS_ONE("1 + 1", "", "one_plus_one"),
	GOOD_PRICE("Хорошая цена", "", "good_price");
	
	private String name;
	private String tooltip;
	private String synonym;
	
	TypeCode(String name, String tooltip, String synonym) {
		this.name = name;
		this.tooltip = tooltip;
		this.synonym = synonym;
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getTooltip() {
		return this.tooltip;
	}
	
	public String getSynonym() {
		return this.synonym;
	}
}
