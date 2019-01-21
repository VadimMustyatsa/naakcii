package naakcii.by.api.country;

//Codes of countries in ISO 3166-1 Alpha-2 format.
public enum CountryCode {
	
	AU("AUS", "Австралия"),
	AT("AUT", "Австрия"),
	AZ("AZE", "Азербайджан"),
	AL("ALB", "Албания"),
	DZ("DZA", "Алжир"),
	AO("AGO", "Ангола"),
	AD("AND", "Андорра"),
	AR("ARG", "Аргентина"),
	AM("ARM", "Армения"),
	AF("AFG", "Афганистан"),
	BD("BGD", "Бангладеш"),
	BY("BLR", "Беларусь"),
	BE("BEL", "Бельгия"),
	BG("BGR", "Болгария"),
	BA("BIH", "Босния и Герцеговина"),
	BR("BRA", "Бразилия"),
	GB("GBR", "Великобритания"),
	HU("HUN", "Венгрия"),
	VE("VEN", "Венесуэла"),
	VN("VNM", "Вьетнам"),
	GH("GHA", "Гана"),
	GT("GTM", "Гватемала"),
	DE("DEU", "Германия"),
	HN("HND", "Гондурас"),
	GR("GRC", "Греция"),
	GE("GEO", "Грузия"),
	DK("DNK", "Дания"),
	EG("EGY", "Египет"),
	IL("ISR", "Израиль"),
	IN("IND", "Индия"),
	ID("IDN", "Индонезия"),
	JO("JOR", "Иордания"),
	IQ("IRQ", "Ирак"),
	IR("IRN", "Иран"),
	IE("IRL", "Ирландия"),
	IS("ISL", "Исландия"),
	ES("ESP", "Испания"),
	IT("ITA", "Италия"),
	KZ("KAZ", "Казахстан"),
	CM("CMR", "Камерун"),
	CA("CAN", "Канада"),
	KE("KEN", "Кения"),
	CY("CYP", "Кипр"),
	CG("CGZ", "Киргизия"),
	TW("TWN", "Китайская Республика"),
	CN("CHN", "Китайская Народная Республика"),
	CO("COL", "Колумбия"),
	CR("CRI", "Коста-Рика"),
	CI("CIV", "Кот-д’Ивуар"),
	CU("CUB", "Куба"),
	LV("LVA", "Латвия"),
	LB("LBN", "Ливан"),
	LT("LTU", "Литва"),
	LI("LIE", "Лихтенштейн"),
	LU("LUX", "Люксембург"),
	MA("MAR", "Марокко"),
	MX("MEX", "Мексика"),
	MD("MDA", "Молдавия"),
	NE("NER", "Нигер"),
	NG("NGA", "Нигерия"),
	NL("NLD", "Нидерланды"),
	NI("NIC", "Никарагуа"),
	NZ("NZL", "Новая Зеландия"),
	NO("NOR", "Норвегия"),
	PK("PAK", "Пакистан"),
	PA("PAN", "Панама"),
	PY("PRY", "Парагвай"),
	PE("PER", "Перу"),
	PL("POL", "Польша"),
	PT("PRT", "Португалия"),
	KR("KOR", "Республика Корея"),
	RU("RUS", "Россия"),
	RO("ROU", "Румыния"),
	SA("SAU", "Саудовская Аравия"),
	RS("SRB", "Сербия"),
	SY("SYR", "Сирия"),
	SK("SVK", "Словакия"),
	SI("SVN", "Словения"),
	US("USA", "США"),
	TJ("TJK", "Таджикистан"),
	TH("THA", "Таиланд"),
	TN("TUN", "Тунис"),
	TM("TKM", "Туркмения"),
	TR("TUR", "Турция"),
	UZ("UZB", "Узбекистан"),
	UA("UKR", "Украина"),
	UY("URY", "Уругвай"),
	PH("PHL", "Филиппины"),
	FI("FIN", "Финляндия"),
	FR("FRA", "Франция"),
	HR("HRV", "Хорватия"),
	ME("MNE", "Черногория"),
	CZ("CZE", "Чехия"),
	CL("CHL", "Чили"),
	CH("CHE", "Швейцария"),
	SE("SWE", "Швеция"),
	EC("ECU", "Эквадор"),
	EE("EST", "Эстония"),
	ZA("ZAF", "ЮАР"),
	JM("JAM", "Ямайка"),
	JP("JPN", "Япония");
	
	private String alphaCode3;
	private String countryName;
	
	CountryCode(String alphaCode3, String countryName) {
		this.alphaCode3 = alphaCode3;
		this.countryName = countryName;
	}
	
	public String getAlphaCode2() {
		return name();
	}
	
	public String getAlphaCode3() {
		return this.alphaCode3;
	}
	
	public String getCountryName() {
		return this.countryName;
	}
}
