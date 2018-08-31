import {Component, ElementRef, OnInit, EventEmitter, ChangeDetectionStrategy} from '@angular/core';
import {Cart, CartLine} from '../shared/cart/cart.model';
import {FoodsStorageService} from '../shared/Storage/foods.storage.service';
import {isUndefined} from 'util';
import {FoodList} from '../shared/foodList/foods.foodList.model';
import {Chain, ChainLine} from '../shared/chain/chain.model';
import {MaterializeAction} from 'angular2-materialize'
import { Title } from '@angular/platform-browser'
import {Router} from "@angular/router";
import {PdfGeneratorService} from "../shared/services/pdf-generator.service";
import {UndiscountService} from "../shared/services/undiscount.service";

@Component({
  selector: 'app-finalize-page',
  templateUrl: './finalize-page.component.html',
  styleUrls: ['./finalize-page.component.scss'],
  providers: [FoodsStorageService],
  changeDetection: ChangeDetectionStrategy.OnPush

})
export class FinalizePageComponent implements OnInit {
  chainListExist: ChainLine[] = null;
  widthContainer = 1200;
  undiscount: Array<{text:string; id: string}> ;

  params = [
    {
      onOpen: (el) => {
      el.prevObject[0].querySelector('.arrowCollapsibleBold').innerHTML = 'arrow_drop_down';
      },
      onClose: (el) => {
        el.prevObject[0].querySelector('.arrowCollapsibleBold').innerHTML = 'arrow_right';
      }
    }
  ];
  modalActions = new EventEmitter<string|MaterializeAction>();
  openModal() {
    this.modalActions.emit({action: 'modal', params: ['open']});
  }
  closeModal() {
    this.modalActions.emit({action: 'modal', params: ['close']});
  }
  constructor(private router: Router ,public  chainLst: Chain,
              private el: ElementRef,
              private undiscountStorage:UndiscountService,
              public cart: Cart, private titleService: Title, private PDFGenerator: PdfGeneratorService) {
    window.scrollTo(0,0);
    this.undiscount = this.undiscountStorage.getFromUndiscount() || [];
  }

  ngOnInit() {
    this.chainListExist = this.getExistListChain();
    this.titleService.setTitle('Список покупок – НаАкции.Бел');

  }

  setImgStyles(pict) {
    return {
      'background-image': `url("assets/images/Products/${pict}")`,
      'background-size': 'contain',
      'background-repeat': 'no-repeat',
      'background-position': 'center'
    };
  }

  getExistListChain() {
    const chainListExist: ChainLine[] = [];
    this.cart.lines.map(line => {
      if (isUndefined(chainListExist.find(x => x.chain.id === line.product.idStrore))) {
        chainListExist.push(this.getStorageByID(line.product.idStrore));
      }
    });
    return chainListExist;
  }

  getCartByChain(idChain: number): CartLine[] {
    const cartListByChain: CartLine[] = [];
    this.cart.lines.map(line => {
      if (line.product.idStrore == idChain) {
        cartListByChain.push(line);
      }
    });
    return cartListByChain;
  }

  getCartQuantityByChain(idChain: number) {
    let quantity = 0;
    this.cart.lines.map(line => {
      if (line.product.idStrore === idChain) {
        quantity += line.quantity;
      }
    });
    return quantity;
  }

  getCartAllPriceByChain(idChain: number) {
    let allPrice = 0;
    this.cart.lines.map(line => {
      if (line.product.idStrore === idChain) {
        if (line.product.allPrice > 0) {
          allPrice += (line.product.allPrice * line.quantity);
        } else {
          allPrice += (line.product.totalPrice * line.quantity);
        }
      }
    });
    return allPrice;
  }

  getCartTotalPriceByChain(idChain: number) {
    let totalPrice = 0;
    this.cart.lines.map(line => {
      if (line.product.idStrore === idChain) {
        totalPrice += (line.product.totalPrice * line.quantity);
      }
    });
    return totalPrice;
  }

  getSumDiscount(food: FoodList) {
    return (food.allPrice - food.totalPrice).toFixed(2);
  }

  getStorageByID(id: number): ChainLine {
    return this.chainLst.lines.find(x => x.chain.id === id);
  }

  getNameStorage(id: number): String {
    if (this.getStorageByID(id)) {
      return this.getStorageByID(id).chain.name;
    }
    return 'unknown';
  }

  deleteCartLine(cartLine: CartLine) {
    this.cart.removeLine(cartLine.product.id);
    this.chainListExist = this.getExistListChain();
  }

  subItem(curFood: CartLine) {
    if (curFood.quantity > 1) {
      this.cart.updateQuantity(curFood.product, Number(curFood.quantity - 1));
    }
  }

  addItem(curFood: CartLine) {
    this.cart.updateQuantity(curFood.product, Number(curFood.quantity + 1));
  }

  onResizeContent() {

    const rootElement = this.el.nativeElement;
    const childElement = rootElement.firstElementChild;
    const contentElement = childElement.firstElementChild;
    this.widthContainer = contentElement.clientWidth;
  }

  getPosition() {
    this.onResizeContent();
    const clientHeight = document.documentElement.clientHeight; // высота видимой части
    const offsetHeight = document.documentElement.offsetHeight;
    if ((offsetHeight - clientHeight) < 50) {
      return '';
    } else {
      return 'fixed';
    }
  }

  onRemoveUndiscount(event){
     this.undiscount.forEach((i,index)=>{
       if(event.target.parentNode.id === i.id.toString()){
         this.undiscount.splice(index,1);
       }
     });
    this.undiscountStorage.setToUndiscount(this.undiscount);
  };

  onAddUndiscount(event){
    let value = event.target.parentNode.previousElementSibling.value;
    if( value.length>2 && value.length<50) {
      this.undiscount.push({
        text: value,
        id: event.timeStamp
      });
    }
    event.target.parentNode.previousElementSibling.value='';
    this.undiscountStorage.setToUndiscount(this.undiscount);
  }

  AddUndiscountByEnter(event){
    if(event.keyCode === 13){
      let value = event.target.value;
      if( value.length>2 && value.length<50) {
        this.undiscount.push({
          text: value,
          id: event.timeStamp
        });
      }
      event.target.value='';
      this.undiscountStorage.setToUndiscount(this.undiscount);
    }
  }

  onRedirect(){
    this.closeModal();
    sessionStorage.clear();
    this.cart.lines = [];
    this.router.navigateByUrl('/form-shopping-list');
  }
  generatePDF() {
    this.PDFGenerator.generatePDF();
  }
  onEventStop(event) {
    event.stopPropagation();
  }
}


