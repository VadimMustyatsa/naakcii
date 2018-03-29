package by.naakcii.repository.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "CHAIS_AND_PRODUCTS")
@org.hibernate.annotations.Immutable
public class ChainsAndProducts {

	@Embeddable
	public static class Id implements Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = 6978249943191744201L;

		@Column(name = "PRODUCT_ID")
		private Long productId;
		
		@Column(name = "CHAIN_ID")
		private Long chainId;
		
		public Id() {
			
		}
		
		public Id(Long productId, Long chainId) {
			this.productId = productId;
			this.chainId = chainId;
		}
		
		public boolean equals(Object o) {
			if(o != null && o instanceof Id ) {
				Id that = (Id) o;
				return this.productId.equals(that.productId) && 
						this.chainId.equals(that.chainId);
			}
			return false;
		}
		
		public int hashCode() {
			return productId.hashCode() + chainId.hashCode();
		}
		
	}
	
	@EmbeddedId
	private Id id = new Id();
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(
			name = "PRODUCT_ID",
			updatable = false,
			insertable = false
	)
	private Product product;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(
			name = "CHAIN_ID",
			updatable = false,
			insertable = false
	)
	private Chain chain;
	
	public ChainsAndProducts() {
		
	}
	
	public ChainsAndProducts(Product product, Chain chain) {
		this.product = product;
		this.chain = chain;
		this.id.productId = product.getId();
		this.id.chainId = chain.getId();
		product.getChainsAndProducts().add(this);
		chain.getChainsAndProducts().add(this);
	}

	public Id getId() {
		return id;
	}

	public void setId(Id id) {
		this.id = id;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public Chain getChain() {
		return chain;
	}

	public void setChain(Chain chain) {
		this.chain = chain;
	}
	
}
