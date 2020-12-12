import React from 'react';
import './index.less'
import '../../../global.less'
import {Badge, Col, Row, UncontrolledTooltip} from "reactstrap";
// @ts-ignore
import 'react-toastify/dist/ReactToastify.css';

interface initialState {

}

// @ts-ignore
class CurrentOrderLinksOneComponent extends React.Component {
  state: initialState;
  props: any

  constructor(props: any) {
    super(props);
    this.state = {
      // isChecked: false,
    }
  }

  render() {
    const {orders, changeOrder, pushNextModal, setMyState, setShowHistoryFunctionState, showHistoryModal, changeToPayed} = this.props
    return (
      <div className="currentOrderLinksZeroComponent">
        <div className="mt-4">
          <Row>
            {orders.map((item: any, index: number) =>
              <Col md={6} className="my-3">
                <Row className="m-0 orderCardPartMain">
                  <Col md={1} className="orderCardPartOne">
                    <div className="d-flex align-items-center justify-content-center h-100">
                      <div className="">
                        <p className="titlePartOne fs-15 font-family-semi-bold mb-0">{item.uniqueId}</p>
                      </div>
                    </div>
                  </Col>
                  <Col md={9} className="orderCardPartTwo">
                    <Row>
                      <Col md={5} className="sub1">
                        <div className="text-center">
                          <p className="fs-17 font-family-bold mb-0">Foydalanuvchi</p>
                          <hr className="hrStyle"/>
                        </div>
                        <div className="d-flex align-items-center">
                          <div className="h-100">
                            <div className="d-flex align-items-center h-100 mb-2 mt-1 userInfos">
                              <div className="">
                                <img
                                  src={item.resUser.photoId ? "/api/file/" + item.resUser.photoId : "https://planetfashion.imgix.net/resources/img/planet_fashion/Denim_121219.jpg?auto=format"}
                                  alt=""/>
                              </div>
                              <div className="ml-2">
                                <p
                                  className="userFullName fs-13 font-family-light mb-0">{item.resUser.fullName ? item.resUser.fullName : ""}</p>
                              </div>
                            </div>
                            <div className="dataPartTwoMain">
                              <div className="">
                                <p className="dataPartTwo phone d-flex align-items-center h-100">
                                <span className="icon icon-phone mr-2"
                                      style={{width: "17px", height: "17px", background: "#000"}}/>
                                  <span
                                    className="fs-15 font-family-light">{item.resUser.phoneNumber ? item.resUser.phoneNumber : ""}</span>
                                </p>
                                {item.resUser.additionalPhone ?
                                  <p className="dataPartTwo phone d-flex align-items-center h-100">
                                <span className="icon icon-phone mr-2"
                                      style={{width: "17px", height: "17px", background: "#000"}}/>
                                    <span
                                      className="fs-15 font-family-light">{item.resUser.additionalPhone ? item.resUser.additionalPhone : ""}</span>
                                  </p> : ""}
                              </div>
                            </div>
                          </div>
                        </div>
                      </Col>
                      <Col md={6} className="sub2">
                        <div className="text-center">
                          <p className="fs-17 font-family-bold mb-0">Mahsulot</p>
                          <hr className="hrStyle"/>
                        </div>
                        <div className="d-flex align-content-start h-100 flex-column">
                          <div className="d-flex align-items-center">
                            <div className="">
                              {item.payed ? <div className="d-flex align-items-center h-100">
                                  <div className="point2"/>
                                  <div className="mx-2">
                                    <p className="fs-16 font-family-light mb-0">To`langan</p>
                                  </div>
                                </div> :
                                <div className="d-flex align-items-center h-100">
                                  <div className="point3"/>
                                  <div className="mx-2">
                                    <p className="fs-16 font-family-light mb-0">To`lanmagan</p>
                                  </div>
                                </div>}
                              <p className="dataPartTwo user d-flex align-items-center h-100">

                                Umumiy buyurtma miqdori:
                                <Badge color="primary mt-1"
                                       pill> {item.resSelectedProducts.reduce((a: number, {count}: { count: number }) => a + count, 0)}  </Badge> kg
                              </p>
                            </div>
                          </div>

                          <div
                            className='d-inline edit-tool filter-tool position-relative d-flex justify-content-start'>
                            <div className="mt-2 total">
                              <p className="fs-30 font-family-semi-bold">
                                {item.resSelectedProducts.reduce((a: number, {calculatedPrice}
                                  : { calculatedPrice: number }) => a + calculatedPrice, 0)} so'm
                              </p>
                            </div>
                            <div style={{left: -242, top: 59}}
                                 className="edit-tool-con text-left filter-design w-max position-absolute py-3 px-1">
                              <div className="triangle" style={{left: 250}}/>
                              <div className="container">
                                {item.resSelectedProducts.map((res: any) => {
                                  return <div className="row mb-1 mt-2">
                                    <div className="col-md-12">
                                      <p className="mb-0 fs-12 font-family-semi-bold">Mahsulot - {res.product.name}</p>
                                    </div>
                                    <div className="col-md-12">
                                      <p className="mb-0 fs-12 font-family-semi-bold">Hisoblangan narxi
                                        - {res.calculatedPrice} so'm</p>
                                    </div>
                                    <div className="col-md-12">
                                      <p className="mb-0 fs-12 font-family-semi-bold">Buyrutma miqdori - {res.count} kg</p>
                                    </div>
                                    <div className="col-md-12">
                                      {/*<p className="mb-0 fs-12 font-family-semi-bold">Mahsulot turi*/}
                                      {/*  : {res.resProductPrice.resQuantity.value} {res.resProductPrice.resQuantity.quantityType} </p>*/}

                                      <p className="mb-0 fs-12 font-family-semi-bold">Mahsulotning 1kg narxi
                                        : {res.resProductPrice.price} so'm</p>
                                    </div>

                                    <hr/>
                                  </div>
                                })}
                              </div>
                            </div>
                          </div>
                        </div>
                      </Col>
                    </Row>
                  </Col>
                  <Col md={1} className="orderCardPartThree">
                    <div className="h-100">
                      <div className="d-flex align-items-center h-100">
                        <div className="">
                          <div className="d-flex align-items-center my-2">
                            <div className="change-order-btn btn-size-md">
                              <button
                                id="history"
                                onClick={() => {
                                  showHistoryModal(index);
                                  setShowHistoryFunctionState("showHistoryFunction")
                                }}>
                                <span className="icon icon-chat-history-line"/>
                              </button>
                              <UncontrolledTooltip placement="right" target="history">
                                Buyurtmani tarixini ko`rish
                              </UncontrolledTooltip>
                            </div>
                            <div className="change-order-btn btn-size-md ml-3">
                              <button
                                id="reject"
                                onClick={
                                  () => {
                                    pushNextModal();
                                    setMyState("myFunction",
                                      () => changeOrder(item.userOrderId, 3))
                                  }}>
                                <span className="icon icon-rubbish"/>
                              </button>
                              <UncontrolledTooltip placement="right" target="reject">
                                Buyurtmani bekor qilish
                              </UncontrolledTooltip>
                            </div>
                          </div>
                          <div className="d-flex align-items-center my-2">
                            <div className="change-order-btn btn-size-md">
                              <button
                                id="toCreated"
                                onClick={() => {
                                  pushNextModal();
                                  setMyState("myFunction", () => changeOrder(item.userOrderId, 0))
                                }}>
                                <span className="icon icon-arrow-left-line"/>
                              </button>
                              <UncontrolledTooltip placement="right" target="toCreated">
                                Buyurtmani yangi bosqichga qaytarish
                              </UncontrolledTooltip>
                            </div>
                            <div className="change-order-btn btn-size-md ml-3">
                              <button
                                id="toClosed"
                                onClick={
                                  () => {
                                    pushNextModal();
                                    setMyState("myFunction",
                                      () => changeOrder(item.userOrderId, 2))
                                  }}>
                                <span className="icon icon-arrow-right-line"/>
                              </button>
                              <UncontrolledTooltip placement="right" target="toClosed">
                                Buyurtmani yopish
                              </UncontrolledTooltip>
                            </div>
                          </div>
                          <div className="d-flex align-items-center my-2">
                            {item.payed ? <div className="change-order-btn btn-size-md ml-3">
                                <button onClick={() => {
                                  pushNextModal();
                                  setMyState("myFunction", () => changeToPayed(item.userOrderId, false, 1))
                                }}>
                                  <span id="popover1" className="icon icon-block"/>
                                  <UncontrolledTooltip placement="right" target="popover1">
                                    To`lovni bekor qilish
                                  </UncontrolledTooltip>
                                </button>

                              </div>
                              :
                              <div className="change-order-btn btn-size-md ml-3">
                                <button onClick={() => {
                                  pushNextModal();
                                  setMyState("myFunction", () => changeToPayed(item.userOrderId, true, 1))
                                }}>
                                  <span id="popover2" className="icon icon-bank-card-fill"/>
                                  <UncontrolledTooltip placement="right" target="popover2">
                                    To`lov qilish
                                  </UncontrolledTooltip>
                                </button>

                              </div>
                            }
                          </div>

                        </div>
                      </div>
                    </div>
                  </Col>
                </Row>
              </Col>
            )}
          </Row>
        </div>
      </div>
    );
  }
}

export default CurrentOrderLinksOneComponent;
