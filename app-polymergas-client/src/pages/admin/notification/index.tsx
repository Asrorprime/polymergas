import React from 'react';
import './index.less'
import '../../../global.less'
import {Button, Card, CardBody, Col, Modal, ModalBody, Row} from "reactstrap";
import BenefitAdminLinksComponent from "@/component/BenefitAdminLinks/BenefitAdminLinksComponent";
// @ts-ignore
import {AvField, AvForm} from 'availity-reactstrap-validation';
import {connect} from "dva";
// @ts-ignore
import 'react-toastify/dist/ReactToastify.css';
import {getMessageType} from "../../../utils/constant";
import Pagination from "react-js-pagination";

interface initialState {
  [x: string]: any;

}

// @ts-ignore
@connect(({app, notification}) => ({app, notification}))
class Index extends React.Component {
  state: initialState;
  props: any

  constructor(props: any) {
    super(props);
    this.state = {}
  }

  handlePageChange(pageNumber: any) {
    this.setState({activePage: pageNumber});
    const {dispatch} = this.props;
    dispatch({
      type: 'notification/getNotifications',
      payload: {
        page: pageNumber - 1,
        size: 10
      }
    })
  }

  componentDidMount() {
    const {dispatch} = this.props;
    dispatch({
      type: 'notification/getNotifications',
      payload: {
        page: 0,
        size: 10
      }
    });
  }

  render() {
    const {dispatch, app, notification} = this.props;
    const {currentUser, admin, moder} = app;
    const {notifications, currentNotification, modalShow, index, totalElements} = notification;

    const openModal = (id: number) => {
      if (id !== -1) {
        dispatch({
          type: 'notification/getNotification',
          payload: {
            path: id
          }
        }).then((res: { success: any; object: any; }) => {
          if (res.success) {
            dispatch({
              type: "notification/getNotifications",
              payload: {
                page: 0,
                size: 10
              }
            })

          }
        });
      }
      dispatch({
        type: "notification/updateState",
        payload: {
          modalShow: !modalShow,
        }
      });
    };
    const deleteNotification = (id: any) => {
      dispatch({
        type: 'notification/deleteNotification',
        payload: {
          id: id
        }
      }).then((res: { success: any; }) => {
          if (res.success) {
            // @ts-ignore
            openModal(-1);
            dispatch({
              type: "notification/getNotifications",
              payload: {
                page: 0,
                size: 10
              }
            })
          }
        }
      )

    };
    const readNotification = (id: any) => {
      dispatch({
        type: 'notification/readNotification',
        payload: {
          path: id
        }
      }).then((res: { success: any; }) => {
        if (res.success) {
          dispatch({
            type: 'notification/getUserAllNotifications'
          })
          dispatch({
            type: 'app/getUserUnreadNotifications'
          })
        }
      })
    };
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
                    <p className="fs-12 pl-3 font-family-semi-bold mb-0">
                      Bildirishnomalar
                    </p>
                  </div>
                </div>
                <div className="col-lg-10 d-none-mb h-88">
                  <div className="d-flex align-items-center h-100 ml-3">
                    {/*<AvForm className="search-form">*/}
                    {/*  <div className="input-group">*/}
                    {/*    <AvField type="text" className="form-control" name="searchName"*/}
                    {/*             id="search"*/}
                    {/*             placeholder="Qidirish"/>*/}
                    {/*    <div className="input-group-append">*/}
                    {/*      <div className="">*/}
                    {/*        <div className="d-flex justify-content-end">*/}
                    {/*          <div className="">*/}
                    {/*            <button type="submit" className="searchIcon">*/}
                    {/*              <span className="icon icon-search"/>*/}
                    {/*            </button>*/}
                    {/*          </div>*/}
                    {/*        </div>*/}
                    {/*      </div>*/}
                    {/*    </div>*/}
                    {/*  </div>*/}
                    {/*</AvForm>*/}
                  </div>
                </div>
              </div>
            </div>
            <Row className="m-0 mt-5">
              <Col md={12}>
                <div className="notificationSection" style={{marginBottom: "100px"}}>
                  <div>
                    <div className="">
                      <div className="d-flex notificationNoteMain align-items-center h-100 mb-3 mt-1">
                        <div className="">
                          <p className="mb-0 fs-17 font-family-semi-bold">Bildirishnomalar</p>
                        </div>
                      </div>
                    </div>
                    {notifications.length > 0 ? notifications.map((item, i) =>
                      <div>
                        <Card onClick={() => openModal(item.notificationId, i)} className="notificationCard mb-3">
                          <CardBody>
                            <div className="">
                              <div className="d-flex align-items-center h-100 w-100">
                                {item.seen === false ?
                                  <div className="">
                                    <span className="pulse"/>
                                  </div> : ''
                                }
                                <div className="w-100">
                                  <div className="notificationNessages">
                                    <div className="notificationTextt">
                                      <div className="ellipsesLine2">
                                        <div className="ellipsesLineBox2">
                                          <Row className="w-100">
                                            <Col md={12} className="w-100">
                                              <p className="userName mb-0">
                                                {getMessageType(item.messageType)}
                                              </p>
                                              <p className="notificationCardText3 ellipsesLineText2 mb-0 w-100">
                                                {item.message}
                                              </p>
                                              <p className="notificationCardText3 ellipsesLineText2 mb-0 w-100">
                                                Buyurtmaning raqami :{item.userOrderNumber}
                                              </p>
                                            </Col>
                                          </Row>
                                        </div>
                                      </div>
                                    </div>
                                  </div>
                                </div>
                              </div>
                            </div>
                          </CardBody>
                        </Card>
                      </div>
                    ) : <div className="noCardInfo">
                      <Card>
                        <CardBody>
                          <h6 className="mb-0 text-center">Bildirishnomalar mavjud emas</h6>
                        </CardBody>
                      </Card>
                    </div>}

                    {notifications.length>0 ?
                      <div className={totalElements ? "my-paginate mb-3" : 'd-none mb-3'}>
                        <Pagination
                          activePage={this.state.activePage}
                          itemsCountPerPage={10}
                          totalItemsCount={totalElements ? totalElements : ''}
                          pageRangeDisplayed={5}
                          onChange={this.handlePageChange.bind(this)}
                        />
                      </div> : null}
                  </div>

                </div>
              </Col>
            </Row>
          </Col>
          <Modal className="modalSectionsNotification" size="md" isOpen={modalShow} toggle={() => openModal(false)}>
            {currentNotification ? <ModalBody>
              <div className="">
                <div className="d-flex justify-content-between">
                  <div className="mr-3">
                    <p className="mb-0 title">{getMessageType(currentNotification.messageType)}</p>
                    <p>
                      <div className="dataMain d-flex align-items-center h-100">
                        <div className="">
                          <img src="/assets/icons/data.png" alt=""/>
                        </div>
                        <div className="mx-1">
                          <p className="mb-0">{currentNotification.createdAt}</p>
                        </div>
                      </div>
                    </p>
                  </div>
                  {currentNotification.user ? <div className="">
                    <div className="">
                      <div className="d-flex align-items-center h-100">
                        <div className="mx-2">
                          <img className="userImg"
                               src={currentNotification.user.imageUrl ? currentNotification.user.imageUrl : "/assets/images/user-icon.png"}
                               alt=""/>
                        </div>
                        <div className="">
                          <p className="mb-0 text">Polymergas</p>
                          <p className="mb-0 text2">Admin</p>
                        </div>
                      </div>
                    </div>
                  </div> : ''
                  }
                </div>
              </div>
              <div className="">
                <p className="mb-0 description">
                  {currentNotification.message}
                </p>
              </div>
              <div className="">
                <p className="mb-0 description">
                  Buyurtma raqami: {currentNotification.userOrderNumber}
                </p>
              </div>
              <div>
                <div className="notificationCardText2 d-flex justify-content-end mt-3">
                  <div className="">
                    <Button className="deleteBtnBenefit benefitBtn mx-2" type="submit" onClick={() => deleteNotification(currentNotification.notificationId)}>Xabarni oʻchirish</Button>
                    <Button className="benefitBtn addBtnBenefit" onClick={() => openModal(false)}>Bekor qilish</Button>

                  </div>
                </div>
              </div>
            </ModalBody> : <h4>Ma'lumotlar mavjud emas!</h4>
            }
          </Modal>
        </Row>
      </div>
    );
  }
}

export default Index;
