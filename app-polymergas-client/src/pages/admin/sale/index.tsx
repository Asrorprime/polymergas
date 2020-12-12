import React from 'react';
import './index.less'
import '../../../global.less'
import {Button, Col, Modal, ModalBody, ModalHeader, Row} from "reactstrap";
// @ts-ignore
import 'react-toastify/dist/ReactToastify.css';
import BenefitAdminLinksComponent from "@/component/BenefitAdminLinks/BenefitAdminLinksComponent";
import AddSale from "@/pages/admin/sale/addSale/addSale";
import {connect} from "@@/plugin-dva/exports";
import Pagination from "react-js-pagination";
// @ts-ignore
import {AvField, AvForm} from 'availity-reactstrap-validation';
import {toast} from "react-toastify";
import DeleteUnversal from "@/pages/admin/sale/delete";
import {FaEye} from "react-icons/fa";

interface initialState {
  [x: string]: any;

  isOpen: boolean,
  isDel: boolean,
  currentPro: any,
  visible: boolean
}

// @ts-ignore

@connect(({sale}) => ({sale}))
class Index extends React.Component {
  state: initialState;
  props: any

  constructor(props: any) {
    super(props);
    this.state = {
      isOpen: false,
      activePage: 1,
      currentid: null,
      currentSaleId: null,
      minDate: null,
      maxDate: null,
      filter: null,
      isCom: false,
      status: false,
      isDel: false,
      currentPro: null,
      currentSale: null,
      visible: false
    }
  }

  handlePageChange(pageNumber: any) {
    this.setState({activePage: pageNumber});
    const {dispatch} = this.props;
    dispatch({
      type: 'sale/getSale',
      payload: {
        page: pageNumber - 1,
        size: 10
      }
    })
  }

  handlePageChangePart(pageNumber: any) {
    this.setState({activePage: pageNumber});
    const {dispatch} = this.props;
    dispatch({
      type: 'sale/getParticipants',
      payload: {
        saleId: this.state.currentSaleId,
        page: pageNumber - 1,
        size: 10
      }
    })
  }

  search = (a, v) => {
    const {dispatch} = this.props;
    if (v !== '') {
      if (v.length > 3) {
        dispatch({
          type: 'sale/getSale',
          payload: {
            page: 0,
            searchName: v,
            size: 10,
            sortBy: this.state.filter,
            endDate: this.state.maxDate ? `${this.state.maxDate} 09:57:38.994000` : null,
            startDate: this.state.maxDate ? `${this.state.minDate} 09:57:38.994000` : null,
          }
        })
      }
    } else {
      dispatch({
        type: 'sale/getSale',
        payload: {
          page: this.state.activePage - 1,
          size: 10,
          sortBy: this.state.filter,
          endDate: this.state.maxDate ? `${this.state.maxDate} 09:57:38.994000` : null,
          startDate: this.state.maxDate ? `${this.state.minDate} 09:57:38.994000` : null,
        }
      })
    }
  };
  filter = (a, v) => {
    const {dispatch} = this.props;
    this.setState({filter: v})
    dispatch({
      type: 'sale/getSale',
      payload: {
        page: this.state.activePage - 1,
        size: 10,
        sortType: v,
        endDate: this.state.maxDate ? `${this.state.maxDate} 09:57:38.994000` : null,
        startDate: this.state.minDate ? `${this.state.minDate} 09:57:38.994000` : null,
      }
    })
  };
  getByDate = (a, v, status) => {
    const {dispatch} = this.props;
    if (status === 'start') {
      dispatch({
        type: 'sale/getSale',
        payload: {
          page: this.state.activePage - 1,
          size: 10,
          sortBy: this.state.filter,
          startDate: `${v} 09:57:38.994000`,
          endDate: this.state.maxDate ? `${this.state.maxDate} 09:57:38.994000` : null,
        }
      });
      this.setState({minDate: v})
    } else {
      dispatch({
        type: 'sale/getSale',
        payload: {
          page: this.state.activePage - 1,
          size: 10,
          sortBy: this.state.filter,
          startDate: this.state.maxDate ? `${this.state.minDate} 09:57:38.994000` : null,
          endDate: `${v} 09:57:38.994000`,
        }
      });
      this.setState({maxDate: v})
    }
  };


  componentDidMount(): void {
    // @ts-ignore
    const {dispatch} = this.props;
    dispatch({
      type: 'sale/getSale',
      payload: {
        page: 0,
        size: 10
      }
    });
  }

  render() {
    const {sale} = this.props;
    const {className, dispatch} = this.props;
    const {sales, totalElements, participants} = sale;
    const del = (currentMainColor: any) => {
      // @ts-ignore
      this.setState({isDel: !this.state.isDel, currentid: currentMainColor})
    };
    const toggle = (currentMainColor: any) => {
      // @ts-ignore
      this.setState({isOpen: !this.state.isOpen, currentPro: currentMainColor})
    };
    const comfirm = (currentMainColor: any) => {
      this.setState({isCom: !this.state.isCom, currentid: currentMainColor.id, status: !currentMainColor.active})
      this.props.dispatch({
        type: 'sale/getSale',
        payload: {
          page: this.state.activePage - 1,
          size: 10
        }
      })
    };
    const showModal = (currentSale: any) => {
      this.props.dispatch({
        type: 'sale/getParticipants',
        payload: {
          saleId: currentSale.id,
          page: this.state.activePage - 1,
          size: 10
        }
      });
      this.setState({visible: !this.state.visible, currentSaleId: currentSale.id})
    };
    const changeIsActive = (id) => {
      // @ts-ignore
      const {dispatch} = this.props;
      dispatch({
        type: 'sale/changeIsActive',
        payload: {
          path: id
        }
      }).then((res: any) => {
        if (res) {
          this.props.dispatch({
            type: 'sale/getSale',
            payload: {
              page: this.state.activePage - 1,
              size: 10
            }
          })
          toast.success("Muvaffaqiyatli o'zgartirildi!")
        } else {
          toast.success("Xatolik")
        }
      })
    };
    // @ts-ignore
    return (
      <div className="container-fluid dashboard position-relative p-0">
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
                      Aksiya <br/> qo'shish
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
                      <div className="d-flex align-items-center">
                        <AvField type="select" name="searchName"
                                 id="sddsds"
                                 placeholder="Qidirish"
                                 className='filterm bg-light ml-3'
                                 onChange={this.filter}>
                          <option value='all'>Barchasi</option>
                          <option value="byActive">Aktivlar</option>
                          <option value="byNotActive">Aktiv emaslar</option>
                        </AvField>
                      </div>
                      <div className="mx-3">
                        <label className="fs-12 font-family-semi-bold">Dan</label>
                        <AvField max={this.state.maxDate}
                                 onChange={(a, v) => this.getByDate(a, v, 'start')}
                                 name="startDate"
                                 className=''
                                 placeHolder='...dan' type='date'/>

                      </div>
                      <div className="">
                        <label className="fs-12 font-family-semi-bold">Gacha</label>
                        <AvField name="endDate"
                                 min={this.state.minDate}
                                 onChange={(a, v) => this.getByDate(a, v, 'end')}
                                 placeHolder='...gacha' type='date'/>
                      </div>
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
                      <th scope="col" className="text-left fs-11 font-family-bold pl-2">MAVZU</th>
                      <th scope="col" className="text-left fs-11 font-family-bold">1 KG MAHSULOT
                        UCHUN IMTIYOZ
                      </th>
                      <th scope="col" className="text-left fs-11 font-family-bold">DAN</th>
                      <th scope="col" className="text-left fs-11 font-family-bold">GACHA</th>
                      <th scope="col"
                          className="text-left fs-11 font-family-bold">ISHTIROKCHILAR
                      </th>
                      <th scope="col" className="text-left fs-11 font-family-bold">AKTIVMI</th>
                      <th scope="col" className="text-left fs-11 font-family-bold">AMAL</th>
                      <th scope="col" className="text-left fs-11 font-family-bold"/>
                    </tr>
                    </thead>
                    <tbody className="app-information2-user">
                    {sales.length ? sales.map((sale: { files: { fileId: string; }[]; titleUz: React.ReactNode; titleRu: React.ReactNode; descriptionUz: React.ReactNode; descriptionRu: React.ReactNode; id: any; }, inx: string | number | undefined) =>
                      <tr className="all-hover-tr boxShadowCards pointer" key={inx}>
                        <th className="pl-2">
                          <h6>{sale.titleUz}</h6>
                        </th>
                        <th>
                          <h6>{sale.ball}</h6>
                        </th>
                        <th>
                          <h6>{sale.startDate}</h6>
                        </th>
                        <th>
                          <h6>{sale.endDate}</h6>
                        </th>
                        <th>
                          <h6>
                            <div className="text-left">
                              <FaEye onClick={() => showModal(sale)} width="40px" height="40px" cursor="pointer"/>
                            </div>
                          </h6>
                        </th>
                        <th>
                          <p className="statusActiveText mb-0">{sale.active ? "Aktiv" : "Aktiv emas"}</p>
                          <label className="switch">
                            <input id="checkboxinp" type="checkbox"
                                   onChange={() => changeIsActive(sale.id)}
                                   checked={sale.active}/>
                            <div className="slider round"/>
                          </label>
                        </th>
                        <th>
                          <div className="tbody-div d-flex justify-content-start">
                            <div className="change-order-btn btn-size-md">
                              <button onClick={() => toggle(sale)}>
                                <span className="icon icon-edit"/>
                              </button>
                            </div>
                            <div className="change-order-btn btn-size-md ml-3">
                              <button onClick={() => del(sale.id)}>
                                <span className="icon icon-rubbish"/>
                              </button>
                            </div>
                          </div>
                        </th>
                      </tr>
                    ) : <tr className="boxShadowCards">
                      <th colSpan={12}>
                        <h6 className="mb-0 ">Ma'lumot mavjud emas !</h6>
                      </th>
                    </tr>}
                    </tbody>
                  </table>
                </Col>
              </Row>
            </div>
          </Col>
        </Row>
        {sales ?
          <div className={totalElements ? "my-paginate" : 'd-none'}>
            <Pagination
              activePage={this.state.activePage}
              itemsCountPerPage={10}
              totalItemsCount={totalElements ? totalElements : ''}
              pageRangeDisplayed={5}
              onChange={this.handlePageChange.bind(this)}
            />
          </div> : null}
        {this.state.isOpen ?
          <AddSale value={{
            isOpen: this.state.isOpen,
            toggle,
            current: this.state.currentPro ? this.state.currentPro : null
          }}/>
          : ''}
        {this.state.isDel ?
          <DeleteUnversal value={{isOpen: this.state.isDel, toggle: del, currentId: this.state.currentid}}/>
          : ''}
        <div className='right-modal'>
          <Modal id="leftsite" size='xl' centered={true} isOpen={this.state.visible} className={className}>
            <ModalHeader className='font-s-20 position-relative mt-2 pl-4'
                         toggle={() => this.setState({visible: !this.state.visible})}>Aksiya
              ishtirokchilari!</ModalHeader>
            <ModalBody style={{minHeight: '75vh'}}>
              <div className="container-fluid mt-3 p-0">
                <div className="">
                  <Row className="m-0 mt-5">
                    <Col md={12} className="app-information-col table-responsive-sm">
                      <table className="app-information-user">
                        <thead>
                        <tr>
                          <th scope="col" className="text-left fs-11 font-family-bold pl-2">T/r</th>
                          <th scope="col" className="text-left fs-11 font-family-bold pl-2">Ishtirokchi FIO</th>
                          <th scope="col" className="text-left fs-11 font-family-bold">Telefon raqami
                          </th>
                          <th scope="col"
                              className="text-left fs-11 font-family-bold">To'plagan balli
                          </th>
                        </tr>
                        </thead>
                        <tbody className="app-information2-user">
                        {console.log(participants)}
                        {participants.length ? participants.map((part, inx: string | number | undefined) =>
                          <tr className="all-hover-tr boxShadowCards pointer" key={inx}>
                            <th className="pl-2">
                              <h6>{inx + 1}</h6>
                            </th>
                            <th className="pl-2">
                              <h6>{part.resUser.fullName}</h6>
                            </th>
                            <th>
                              <h6>{part.resUser.phoneNumber}</h6>
                            </th>
                            <th>
                              <h6>{part.userBall}</h6>
                            </th>

                          </tr>
                        ) : <tr className="boxShadowCards">
                          <th colSpan={12}>
                            <h6 className="mb-0 ">Ma'lumot mavjud emas !</h6>
                          </th>
                        </tr>}
                        </tbody>
                      </table>
                      {participants ?
                        <div className={totalElements ? "my-paginate" : 'd-none'}>
                          <Pagination
                            activePage={this.state.activePage}
                            itemsCountPerPage={10}
                            totalItemsCount={totalElements ? totalElements : ''}
                            pageRangeDisplayed={5}
                            onChange={this.handlePageChangePart.bind(this)}
                          />
                        </div> : null}
                    </Col>
                  </Row>
                </div>

              </div>
            </ModalBody>
          </Modal>
        </div>
      </div>
    );
  }
}

export default Index;
