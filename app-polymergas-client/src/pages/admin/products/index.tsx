import React from 'react';
import './index.less'
import {Button, Col, CustomInput, Row} from "reactstrap";
import BenefitAdminLinksComponent from "@/component/BenefitAdminLinks/BenefitAdminLinksComponent";
import AddProduct from './addProduct'
import {connect} from "@@/plugin-dva/exports";
// @ts-ignore
import Pagination from "react-js-pagination";
import DeleteUnversal from './delete';
import Comfirm from "@/pages/admin/products/Comfirm";
// @ts-ignore
import {AvField, AvForm} from 'availity-reactstrap-validation';
import {toast} from "react-toastify";

interface initialState {
  [x: string]: any;

  isOpen: boolean,
  isDel: boolean,
  active: number,
  filter: null,
  currentid: any,
  currentPro: any
  isCom: boolean,
  status: boolean,
}

// @ts-ignore
@connect(({app}) => ({app}))
class Index extends React.Component {
  // @ts-ignore
  state: initialState;
  props: any;

  constructor(props: any) {
    super(props);
    this.state = {
      isOpen: false,
      isCom: false,
      filter: null,
      status: false,
      isDel: false,
      active: 1,
      currentid: null,
      currentPro: null,
    }
  }

  handlePageChange(pageNumber: any) {
    this.setState({activePage: pageNumber});
    const {dispatch} = this.props;
    dispatch({
      type: 'app/getProduct',
      payload: {
        page: pageNumber - 1,
        size: 10
      }
    })
  }


  componentDidMount(): void {
    const {dispatch} = this.props;
    dispatch({
      type: 'app/getProduct',
      payload: {
        page: 0,
        size: 10
      }
    })
  }

  search = (a, v) => {
    const {dispatch} = this.props;
    if (v !== '') {
      dispatch({
        type: 'app/getProduct',
        payload: {
          page: 0,
          searchName: v,
          size: 10,
          sortType: this.state.filter
        }
      })
    } else {
      dispatch({
        type: 'app/getProduct',
        payload: {
          page: this.state.active - 1,
          size: 10,
          sortType: this.state.filter
        }
      })
    }
  };
  filter = (a, v) => {
    const {dispatch} = this.props;
    this.setState({filter: v})
    dispatch({
      type: 'app/getProduct',
      payload: {
        page: this.state.active - 1,
        size: 10,
        sortType: v
      }
    })
  };

  render() {
    const {dispatch} = this.props;
    const {products, totalElements} = this.props.app;
    const toggle = (currentMainColor: any) => {
      this.setState({isOpen: !this.state.isOpen, currentPro: currentMainColor})
    };
    const del = (currentMainColor: any) => {
      this.setState({isDel: !this.state.isDel, currentid: currentMainColor})
    };
    const comfirm = (currentMainColor: any) => {
      this.setState({isCom: !this.state.isCom, currentid: currentMainColor.id, status: !currentMainColor.new})
      if (currentMainColor === 'success') {
        this.props.dispatch({
          type: 'app/getProduct',
          payload: {
            page: this.state.active - 1,
            size: 10
          }
        })
      }
    };

    const changeProductPriceHaveStatus = (id: any, have: boolean) => {
      dispatch({
        type: 'app/changeProductPriceHaveStatus',
        payload: {
          path: id,
          have: have
        }
      }).then((res: any) => {
          toast.success(res.message);
          dispatch({
            type: 'app/getProduct',
            payload: {
              page: 0,
              size: 10
            }
          })
        }
      )
    };

// @ts-ignore
// @ts-ignore
    return (
      <div className="container-fluid position-relative p-0">
        <div className="position-absolute menuLinkIcon">
          <BenefitAdminLinksComponent/>
        </div>
        <Row className="m-0">
          <Col md={12} className="p-0">
            <div className="app-navbar">
              <div className="row m-0">
                <div className="col-12 col-lg-2">
                  <div className="d-flex align-items-center h-100">
                    <Button className="benefitRoundBtn" onClick={toggle}>
                      <span className="icon icon-plus"/>
                    </Button>
                    <p className="fs-12 pl-3 font-family-semi-bold mb-0">
                      Mahsulot <br/> qo'shish
                    </p>
                  </div>
                </div>
                <div className="col-lg-10 d-none-mb h-88">
                  <div className="d-flex align-items-center h-100 ml-3">
                    <AvForm className="search-form">
                      <div className="input-group">
                        <AvField type="text" className="form-control" name="searchName"
                                 id="search"
                                 placeholder="Qidirish"
                                 onChange={this.search}
                        />
                        <div className="input-group-append">
                          <div className="">
                            <div className="d-flex justify-content-end">
                              <div className="">
                                <button type="submit" className="searchIcon">
                                  <span className="icon icon-search"/>
                                </button>
                              </div>
                            </div>
                          </div>
                        </div>
                      </div>
                    </AvForm>
                    <AvForm className='input-group'>
                      <AvField type="select" name="searchName"
                               id="sddsds"
                               placeholder="Qidirish"
                               className='filterm bg-light ml-3'
                               onChange={this.filter}
                      >
                        <option value="all">Barchasi</option>
                        <option value="byCreatedAsc">Tizimga qo'shilgan vaqti o'sish</option>
                        <option value="byCreatedDesc">Tizimga qo'shilgan vaqti kamayish</option>
                        <option value="byName">Nomi bo'yicha saralash</option>
                        <option value="byNotHaveProduct">Sotuvda yo'q mahsulotlar</option>
                        <option value="byHaveProduct">Sotuvda bor mahsulotlar</option>

                      </AvField>
                    </AvForm>
                  </div>
                </div>
              </div>
            </div>
            <div className="">

              <Row className="m-0 mt-5">
                <Col md={12} className="app-information-col table-responsive-sm">
                  <table className="app-information-user">
                    <thead>
                    <tr>
                      <th scope="col" className="text-left fs-11 font-family-bold pl-2">RASMI</th>
                      <th scope="col" className="text-left fs-11 font-family-bold">NOMI</th>
                      <th scope="col" className="text-left fs-11 font-family-bold">ISHLAB
                        CHIQARUVCHI
                      </th>
                      <th scope="col" className="text-left fs-11 font-family-bold">KATEGORIYASI
                      </th>
                      <th scope="col" className="text-left fs-11 font-family-bold">MAHSULOT TIPI
                      </th>
                      <th scope="col" className="fs-11 font-family-bold">SOTUV HOLATI</th>
                      <th scope="col" className="fs-11 font-family-bold">1KG NARXI</th>
                      <th scope="col" className="text-left fs-11 font-family-bold">QO`SHIMCHA MA`LUMOTLAR</th>
                      <th scope="col" className="text-left fs-11 font-family-bold"/>
                    </tr>
                    </thead>
                    <tbody className="app-information2-user">
                    {products.length ? products.map((item: any, i: any) =>
                        <tr key={i} className="all-hover-tr boxShadowCards pointer">
                          <th>
                            <div
                              className='d-inline edit-tool filter-tool position-relative ml-2 d-flex justify-content-start'>
                              <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24"
                                   width="16" height="16">
                                <path fill="none" d="M0 0h24v24H0z"/>
                                <path
                                  d="M20 5H4v14l9.292-9.294a1 1 0 0 1 1.414 0L20 15.01V5zM2 3.993A1 1 0 0 1 2.992 3h18.016c.548 0 .992.445.992.993v16.014a1 1 0 0 1-.992.993H2.992A.993.993 0 0 1 2 20.007V3.993zM8 11a2 2 0 1 1 0-4 2 2 0 0 1 0 4z"
                                  fill="rgba(135,135,135,1)"/>
                              </svg>
                              <div className="edit-tool-con text-left filter-design w-max position-absolute py-3 px-1">
                                <div className="triangle"/>
                                <div className="container">
                                  {/*Filter Navbar*/}
                                  <div className="row">
                                    {item.photos ? item.photos.map(image =>
                                      <div
                                        className={item.photos.length === 1 ? "col-12 pl-0 mb-3" : "col-4 pl-0 mb-3"}>
                                        <img src={"/api/file/" + image.fileId}
                                             alt="" className='img-fluid'/></div>
                                    ) : null}
                                  </div>
                                  {/*Filter Navbar*/}
                                </div>
                              </div>
                            </div>
                          </th>
                          <th>
                            <h6>{item.name}</h6>
                          </th>
                          <th>
                            <h6>{item.madeIn}</h6>
                          </th>
                          <th>
                            <h6>{item.resCategory.nameUz}</h6>
                          </th>
                          <th>
                            <h6>{item.resProductType.nameUz}</h6>
                          </th>
                          <th>
                            {
                              item.resProductPrices.length > 0 ?
                                <label className="switch">
                                  <input type="checkbox"
                                         onChange={() => changeProductPriceHaveStatus(item.resProductPrices[0].productPriceId, !item.resProductPrices[0].haveProduct)}
                                         checked={item.resProductPrices[0].haveProduct}/>
                                  <div className="slider round"/>
                                </label> : ''
                            }

                          </th>
                          <th>
                            {item.resProductPrices.length > 0 ? `${item.resProductPrices[0].price} so'm` : ''}
                            {/*<div className='d-inline edit-tool filter-tool position-relative ml-2 d-flex justify-content-start'>*/}
                            {/*    Narxlar*/}
                            {/*  <div className="edit-tool-con text-left filter-design w-max position-absolute py-3 px-1">*/}
                            {/*    <div className="triangle"/>*/}
                            {/*    <div className="container">*/}
                            {/*      <div className="row">{item.resProductPrices.sort(item=>item.resQuantity.value).map((price, inex) =>*/}
                            {/*        <div >*/}
                            {/*          <h6><span*/}
                            {/*            className="badge badge-secondary mb-1">{price.resQuantity.value} kilogrami</span> : {price.price} so'm <br/>*/}
                            {/*          </h6>*/}
                            {/*          <h6><span*/}
                            {/*            className="badge badge-secondary mb-1">Mahsulot sotuvda</span> : {price.haveProduct ? 'Bor' : 'Yo`q'}*/}
                            {/*            <br/></h6>*/}
                            {/*          <label className="switch">*/}
                            {/*            <input key={inex} type="checkbox"*/}
                            {/*                   onChange={() => changeProductPriceHaveStatus(price.productPriceId, !price.haveProduct)}*/}
                            {/*                   checked={price.haveProduct}/>*/}
                            {/*            <div className="slider round"/>*/}
                            {/*          </label>*/}
                            {/*        </div>*/}
                            {/*      )}</div>*/}
                            {/*    </div>*/}
                            {/*  </div>*/}
                            {/*</div>*/}
                          </th>
                          <th>
                            <div
                              className='d-inline edit-tool filter-tool position-relative ml-2 d-flex justify-content-start'>
                              Qo'shimcha ma'lumotlar
                              <div className="edit-tool-con text-left filter-design w-max position-absolute py-3 px-1">
                                <div className="triangle"/>
                                <div className="container">
                                  <div className="row">
                                    <div>
                                      <div>
                                        <h6 className="pl-3 font-family-bold">Izoh(Uz):<br/></h6>
                                        <p className="pl-3">{item.descriptionUz}</p>
                                      </div>
                                      <div>
                                        <h6 className="pl-3 font-family-bold">Izoh(Ru):<br/></h6>
                                        <p className="pl-3">{item.descriptionRu}</p>
                                      </div>
                                      <div>
                                        <h6 className="pl-3 font-family-bold">Izoh(En):<br/></h6>
                                        <p className="pl-3">{item.descriptionEn}</p>
                                      </div>
                                      <div>
                                        <h6 className="pl-3 font-family-bold">Tizimga qo'shilgan vaqti:<br/></h6>
                                        <p className="pl-3">{item.createdAt}</p>
                                      </div>
                                      <div>
                                        <h6 className="pl-3 font-family-bold">Kim tomonidan qo'shilgani:<br/></h6>
                                        <p className="pl-3"> {item.createdBy}</p>
                                      </div>
                                    </div>
                                  </div>
                                </div>
                              </div>
                            </div>
                          </th>
                          <th>
                            <div className="tbody-div d-flex justify-content-start">
                              <div className="change-order-btn btn-size-md">
                                <button
                                  onClick={() => toggle(item)}>
                                  <span className="icon icon-edit"/>
                                </button>
                              </div>
                              <div className="change-order-btn btn-size-md ml-3">
                                <button
                                  onClick={() => del(item.id)}
                                >
                                  <span className="icon icon-rubbish"/>
                                </button>
                              </div>
                            </div>
                          </th>
                        </tr>
                      ) :
                      <tr className="boxShadowCards">
                        <th colSpan={12}>
                          <h6 className="mb-0 ">Ma'lumot mavjud emas !</h6>
                        </th>
                      </tr>}
                    </tbody>
                  </table>
                </Col>
              </Row>
              <div className={totalElements ? "my-paginate" : 'd-none'}>
                <Pagination
                  activePage={this.state.activePage}
                  itemsCountPerPage={10}
                  totalItemsCount={totalElements ? totalElements : ''}
                  pageRangeDisplayed={5}
                  onChange={this.handlePageChange.bind(this)}
                />
              </div>
            </div>
          </Col>
        </Row>
        {this.state.isOpen ?
          <AddProduct
            value={{
              isOpen: this.state.isOpen,
              toggle,
              current: this.state.currentPro ? this.state.currentPro : null
            }}/>
          : ''}
        {this.state.isDel ?
          <DeleteUnversal value={{isOpen: this.state.isDel, toggle: del, currentId: this.state.currentid}}/>
          : ''}
        {this.state.isCom ?
          <Comfirm value={{
            status: this.state.status,
            isOpen: this.state.isCom,
            toggle: comfirm,
            currentId: this.state.currentid
          }}/>
          : ''}
      </div>
    );
  }
}

export default Index;
