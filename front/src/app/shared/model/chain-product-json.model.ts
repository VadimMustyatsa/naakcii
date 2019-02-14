export interface ChainProductFromJson {
    productId: number;
    chainId: number;
    chainName: string;
    name: string;
    unitOfMeasure: string;
    manufacturer: string;
    brand: string;
    countryOfOrigin: string;
    picture: string;
    basePrice: number;
    discountPercent: number;
    discountPrice: number;
    startDate: number;
    endDate: number;
    chainProductType: {name: string,
                      tooltip: string};

}
