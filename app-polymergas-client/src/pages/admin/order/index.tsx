import React from 'react';
import './index.less'
import '../../../global.less'
import {Button, Col, Form, FormGroup, Input, Label, Modal, ModalBody, ModalHeader, Row} from "reactstrap";
import BenefitAdminLinksComponent from "@/component/BenefitAdminLinks/BenefitAdminLinksComponent";
// @ts-ignore
// @ts-ignore
import SelectSearch from 'react-select-search';
// @ts-ignore
import 'react-toastify/dist/ReactToastify.css';
import CurrentOrderLinksZeroComponent
    from "@/component/CurrentOrdersComponent/currentOrderLinksZero/currentOrderLinksZeroComponent";
import CurrentOrderLinksOneComponent
    from "@/component/CurrentOrdersComponent/currentOrderLinksOne/currentOrderLinksOneComponent";
import CurrentOrderLinksTwoComponent
    from "@/component/CurrentOrdersComponent/currentOrderLinksTwo/currentOrderLinksTwoComponent";
import CurrentOrderLinksThreeComponent
    from "@/component/CurrentOrdersComponent/currentOrderLinksThree/currentOrderLinksThreeComponent";
import CurrentOrderLinksFourComponent
    from "@/component/CurrentOrdersComponent/currentOrderLinksFour/currentOrderLinksFourComponent";
// @ts-ignore
import {connect} from "dva";
// @ts-ignore
import {AvField, AvForm} from 'availity-reactstrap-validation';
// @ts-ignore
import makeAnimated from 'react-select/animated';
import posed from "react-pose";
import Pagination from "react-js-pagination";
import {toast} from "react-toastify";
// @ts-ignore

const Content = posed.div({
    closed: {height: 0},
    open: {height: 'auto'}
});

const animatedComponents = makeAnimated();

interface initialState {
    activePage: number,
    open: number | boolean,
    myFunction: any,
    edit: boolean,
    showHistoryFunction: any,
    currentOrderLinks: number,
    message: string,
    price:any,
    priceAdd:boolean,
    addOrderList: any,
    selectedPrice: {
        price: number
        productPriceId: string,
        haveProduct: boolean,
        resQuantity: {
            id: number
            quantityType: string
            value: string
        }
    } | undefined
    reqCart: {
        resUser: any
        reqSelectedProducts: {
            // productColorId: string,
            selectedProduct: any,
            selectedPrice: any,
            selectedProductId: string
            productId: string
            productPriceId: string
            count: number
            product: any
            calculatedPrice: number
            userOrderId: string
        }[];
        userId: string;
        userOrderId: string | null
    }
    sort: string,
    search: string,
}

// @ts-ignore
@connect(({order, app}) => ({order, app}))
class Index extends React.Component {
    state: initialState;
    props: any;

    constructor(props: any) {
        super(props);
        this.state = {
            activePage: 0,
            open: false,
            edit: false,
            reqCart: {
                resUser: undefined,
                reqSelectedProducts: [],
                userId: "",
                userOrderId: null
            },
            sort: "",
            search: "",
            selectedPrice: undefined,
            // selectedProduct: undefined,
            myFunction: undefined,
            showHistoryFunction: undefined,
            currentOrderLinks: 0,
            message: "",
            price:0,
            priceAdd:true,
            addOrderList: [
                {
                    number: ""
                },
            ],
        }

    }

    handlePageChange(pageNumber: any) {
        this.setState({activePage: (pageNumber - 1)});
        this.callFunction("getOrders", "orders", "order", pageNumber - 1, 10, {
            state: "CREATED",
            sort: this.state.sort,
            search: this.state.search
        }, undefined, undefined, true)

    }


    componentDidMount(): void {
        const {dispatch} = this.props;

        this.callFunction("getOrders", "orders", "order", this.state.activePage, 10, {
            state: "CREATED",
            sort: this.state.sort,
            search: this.state.search
        }, undefined, undefined, true);
        this.callFunction(
            "searchUser",
            "users",
            "order",
            this.state.activePage,
            10,
            {search: ""},
            undefined,
            undefined,
            false
        );
        dispatch({
            type: 'app/getProductsForSale',
        })
    }

    setMyState = (key: string, value: any) => {
        this.setState({[key]: value})
    };
    setShowHistoryFunctionState = (key: string, value: any) => {
        this.setState({[key]: value})
    };
    setChangeOrderSumState = (key: string, value: any) => {
        this.setState({[key]: value})
    };
    callFunction = (urlApiName: string, objectName: string | undefined, modelName: string, page: number, size: number, payloadRequest: any, successMessage: string | undefined, errorMessage: string | undefined, save: boolean) => {
        const {dispatch} = this.props
        let payload = {
            ...payloadRequest,
            page,
            size,
            objectName,
            successMessage,
            errorMessage,
            urlApiName,
        };
        return dispatch({
            type: modelName + "/request",
            payload
        })
    };
    handleSubmit = (errors: any, values: any) => {
        this.props.dispatch({
            type: "order/updateState",
            payload: {massage: values.message}
        });
        this.setState({errors, message: values.message});
        this.state.myFunction();
    };
    handleSub = (errors: any, values: any) => {
        this.props.dispatch({
            type: "order/updateState",
            payload: {massage: values.message}
        });
        this.setState({errors, message: values.message, price: values.price,priceAdd:values.priceAdd});
        this.state.myFunction();
    };
    search = (e: any) => {
        if (e === "click") {
            this.callFunction("getOrders", "orders", "order", this.state.activePage, 10, {
                state: this.props.order.state[this.state.currentOrderLinks],
                search: this.state.search,
                sort: this.state.sort
            }, undefined, undefined, true)
        } else {
            this.setState({search: e.target.value})
        }
    };
    sort = (e: any) => {
        this.setState({sort: e.target.value});
        this.callFunction("getOrders", "orders", "order", this.state.activePage, 10, {
            state: this.props.order.state[this.state.currentOrderLinks],
            sort: e.target.value,
            search: this.state.search
        }, undefined, undefined, true)
    };

    render() {
        const {open} = this.state;
        const data = [
            {
                title: "",
            }
        ];
        const {order, dispatch, app} = this.props
        const {orders, addModal, currentOrder, changeModal, nextModal, historyModal, users, orderIndex, totalElements} = order;
        const {products, currentUser} = app;
        const {edit, reqCart} = this.state
        let {currentOrderLinks} = this.state;
        const addOrder = (e: any, v: any) => {
            if (this.state.reqCart && this.state.reqCart.userId === "") {
                toast.error("Mijozni tanlang!");
            } else {
                if (this.state.reqCart.reqSelectedProducts && this.state.reqCart.reqSelectedProducts.length < 1) {
                    toast.error("Maxsulot tanlang!");
                } else {
                    this.callFunction("crateOrder", "orderUser", "order", 0, 10, this.state.reqCart, undefined, undefined, false).then((res: any) => {
                        if (res.success) {
                            this.callFunction("getOrders", "orders", "order", 0, 10, {
                                state: this.props.order.state[currentOrderLinks],
                                sort: this.state.sort,
                                search: this.state.search
                            }, undefined, undefined, true)
                            dispatch({
                                type: 'order/updateState',
                                payload: {
                                    addModal: false
                                }
                            });
                            toast.success(res.message);
                        } else {
                            toast.error(res.message);
                        }
                    })
                }
            }

        };
        const pushNextModal = () => {
            dispatch({
                type: "order/updateState",
                payload: {
                    nextModal: !nextModal,
                }
            });
        };
        const openChangeModal = () => {
            dispatch({
                type: "order/updateState",
                payload: {
                    changeModal: !changeModal,
                }
            });
        };
        const changeToPayed = (id: any, payed: boolean, stepNumber: number) => {
            dispatch({
                type: "order/changeToPayed",
                payload: {
                    step: this.props.order.state[stepNumber],
                    // userOrderHistoryId,
                    userOrderId: id,
                    message: this.state.message,
                    currentStep: this.props.order.state[stepNumber],
                    adminId: currentUser.id,
                    payed: payed
                }
            }).then(res => {
                this.callFunction("getOrders", "orders", "order", this.state.activePage, 10, {
                    state: this.props.order.state[this.state.currentOrderLinks],
                    sort: this.state.sort,
                    search: this.state.search
                }, undefined, undefined, true);
            });

        };
        const changeOrderSum = (userOrderId: string) => {
            dispatch({
                type: "order/changeOrderSum",
                payload: {
                    step: this.props.order.state[0],
                    // userOrderHistoryId,
                    userOrderId: userOrderId,
                    message: this.state.message,
                    currentStep: this.props.order.state[0],
                    adminId: currentUser.id,
                    price: this.state.price,
                    priceAdd: this.state.priceAdd,

                }
            }).then(res => {
                this.callFunction("getOrders", "orders", "order", this.state.activePage, 10, {
                    state: this.props.order.state[this.state.currentOrderLinks],
                    sort: this.state.sort,
                    search: this.state.search
                }, undefined, undefined, true);
            });

        };

        const showHistoryModal = (index: any) => {
            dispatch({
                type: "order/updateState",
                payload: {
                    historyModal: !historyModal,
                }
            });

            dispatch({
                type: 'order/updateState',
                payload: {
                    orderIndex: index
                }
            })

        };
        const openModal = () => {
            this.state.reqCart = {
                resUser: undefined,
                reqSelectedProducts: [],
                userId: "",
                userOrderId: null
            };
            this.state.edit = false;
            this.state.addOrderList = [];
            this.setState(this.state);
            dispatch({
                type: "order/updateState",
                payload: {
                    addModal: !addModal,
                }
            });
        };
        const changeCurrentOrderLinks = (currentOrderLinks: any) => {
            this.setState({currentOrderLinks, activePage: 0});
            this.callFunction("getOrders", "orders", "order", this.state.activePage, 10, {
                state: this.props.order.state[currentOrderLinks],
                sort: this.state.sort,
                search: this.state.search
            }, undefined, undefined, true)

        };
        const changeOrder = (userOrderId: string, stepNumber: number) => {
            let payload = {
                step: this.props.order.state[stepNumber],
                // userOrderHistoryId,
                userOrderId,
                message: this.state.message,
                currentStep: this.props.order.state[stepNumber],
                adminId: currentUser.id,
            };
            this.callFunction("changeStepUserOrder", undefined, "order", this.state.activePage, 10, payload, "O'zgartirildi", "Xatolik", true).then((res: any) => {
                if (res.success) {
                    this.callFunction("getOrders", "orders", "order", this.state.activePage, 10, {
                        state: this.props.order.state[this.state.currentOrderLinks],
                        sort: this.state.sort,
                        search: this.state.search
                    }, undefined, undefined, true)
                }
            })

        };
        const editOrder = (userOrder: any) => {
            openModal();
            let reqCart = userOrder;
            reqCart.userOrderId = userOrder.resUser.id;
            reqCart.reqSellectedProducts = userOrder.resSelectedProducts;

            this.setState({reqCart: reqCart, edit: true})
        };
        const addOrderFunc = () => {

            this.state.reqCart.reqSelectedProducts.push({
                    selectedProduct: undefined,
                    selectedPrice: undefined,
                    selectedProductId: "",
                    productId: "",
                    product: undefined,
                    productPriceId: "",
                    count: 0,
                    calculatedPrice: 0,
                    userOrderId: ""
                }
            );
            this.setState(this.state);

            // @ts-ignore
            this.state.addOrderList.push({number: null,})

            this.setState(this.state);
        };
        // @ts-ignore

        let options: { name: string, value: string }[] = [];
        if (products) {
            products.map((product: { name: string, id: string }) => {
                options.push({name: product.name, value: product.id})
            })
        }
        let usersOptions: { name: string, value: string }[] = [];
        if (users) {
            users.list.map((user: {
                fullName: string,
                id: string
            }) => {
                usersOptions.push({name: user.fullName, value: user.id})
            })
        }
        const searchOnchange = (value: any) => {
            this.state.reqCart.userId = value;
            this.setState(this.state)
        };
        const productChange = (value: any, index: number) => {
            this.state.reqCart.reqSelectedProducts[index].productId = value;
            this.state.reqCart.reqSelectedProducts[index].selectedProduct = products.filter((product: { id: string }) => product.id == value)[0];
            this.setState(this.state);
            this.setState({selectedProduct: products.filter((product: { id: string }) => product.id == value)[0]})

            this.state.reqCart.reqSelectedProducts[index].productPriceId = this.state.reqCart.reqSelectedProducts[index].selectedProduct.resProductPrices[0].productPriceId
            this.state.selectedPrice = this.state.reqCart.reqSelectedProducts[index].selectedProduct.resProductPrices[0]
            this.state.reqCart.reqSelectedProducts[index].selectedPrice = this.state.selectedPrice
            this.setState(this.state)
        };
        const countOnChange = (e: any, index: number) => {
            this.state.reqCart.reqSelectedProducts[index].count = e.target.value;
            this.state.reqCart.reqSelectedProducts[index].calculatedPrice = this.state.reqCart.reqSelectedProducts[index].selectedPrice.price * e.target.value;
            this.setState(this.state)
        };
        const productPriceChange = (e: any, index: any) => {


            this.state.reqCart.reqSelectedProducts[index].productPriceId = e.target.value
            this.state.selectedPrice = this.state.reqCart.reqSelectedProducts[index].selectedProduct.resProductPrices.filter((price: any) => price.productPriceId == e.target.value)[0]
            this.state.reqCart.reqSelectedProducts[index].selectedPrice = this.state.selectedPrice
            this.setState(this.state)

            // this.state.reqCart.reqSelectedProducts[index].productPriceId = e.target.value;
            // this.state.reqCart.reqSelectedProducts[index].selectedProduct = products.filter((product: { id: string }) => product.id == e.target.value)[0];
            // this.state.reqCart.reqSelectedProducts[index].selectedPrice = this.state.selectedPrice;
            // this.setState(this.state)
        };
        const deleteOrderFunc = (isMain: any) => {
            if (isMain === 'item') {
                // @ts-ignore
                if (this.state.addOrderList.length > 1) {
                    // @ts-ignore
                    this.state.addOrderList.pop();
                    this.state.reqCart.reqSelectedProducts.pop();
                }
            }
            this.setState(this.state);
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
                                        <Button className="benefitRoundBtn" onClick={openModal}>
                                            <span className="icon icon-plus"/>
                                        </Button>
                                        <p className="fs-12 pl-3 font-family-semi-bold mb-0">
                                            Buyurtmalar<br/> qo'shish
                                        </p>
                                    </div>
                                </div>
                                <div className="col-lg-10 d-none-mb h-88">
                                    <div className="h-100">
                                        <div
                                            className="reportMainSectionCol1 position-relative d-flex align-items-center h-100">

                                            <div className="mr-5">
                                                <div className="mr-3 mt-n3">
                                                    <div className="position-absolute" style={{zIndex: 4}}>
                                                        {data.map(({title}, i) => (
                                                            <div>
                                                                <div className="change-order-btn btn-size-md ml-3"
                                                                     onClick={() => this.setState({open: open === i ? false : i})}>
                                                                    <button>
                                                                        <span className="icon icon-filter"/>
                                                                    </button>
                                                                </div>
                                                                <Content className="content"
                                                                         pose={open === i ? 'open' : 'closed'}>
                                                                    <div className="content-wrapper">
                                                                        <Form>
                                                                            <FormGroup check>
                                                                                <Label check>
                                                                                    <Input type="radio" name="radio1"
                                                                                           value="userNameAsc"
                                                                                           onChange={this.sort}
                                                                                           checked={"userNameAsc" === this.state.sort}
                                                                                           style={{transform: "translate(0px,1.5px)"}}/>{' '}
                                                                                    <span
                                                                                        className="fs-14 font-family-light">Nom bo`yicha (A-Z)</span>
                                                                                </Label>
                                                                            </FormGroup>
                                                                            <FormGroup check>
                                                                                <Label check>
                                                                                    <Input type="radio" name="radio1"
                                                                                           value="userNameDesc"
                                                                                           onChange={this.sort}
                                                                                           checked={"userNameDesc" === this.state.sort}
                                                                                           style={{transform: "translate(0px,1.5px)"}}/>{' '}
                                                                                    <span
                                                                                        className="fs-14 font-family-light">Nom bo`yicha (Z-A)</span>
                                                                                </Label>
                                                                            </FormGroup>
                                                                            <FormGroup check>
                                                                                <Label check>
                                                                                    <Input type="radio" name="radio1"
                                                                                           value=""
                                                                                           checked={"" === this.state.sort}
                                                                                           onChange={this.sort}
                                                                                           style={{transform: "translate(0px,1.5px)"}}/>{' '}
                                                                                    <span
                                                                                        className="fs-14 font-family-light">Berilgan vaqti o'sish</span>
                                                                                </Label>
                                                                            </FormGroup>
                                                                            <FormGroup check>
                                                                                <Label check>
                                                                                    <Input type="radio" name="radio1"
                                                                                           value="createdAtDesc"
                                                                                           onChange={this.sort}
                                                                                           checked={"createdAtDesc" === this.state.sort}
                                                                                           style={{transform: "translate(0px,1.5px)"}}/>{' '}
                                                                                    <span
                                                                                        className="fs-14 font-family-light">Berilgan vaqti kamayish</span>
                                                                                </Label>
                                                                            </FormGroup>
                                                                            <FormGroup check>
                                                                                <Label check>
                                                                                    <Input type="radio" name="radio1"
                                                                                           value="priceAsc"
                                                                                           onChange={this.sort}
                                                                                           checked={"priceAsc" === this.state.sort}
                                                                                           style={{transform: "translate(0px,1.5px)"}}/>{' '}
                                                                                    <span
                                                                                        className="fs-14 font-family-light">Narxi o'sish</span>
                                                                                </Label>
                                                                            </FormGroup>
                                                                            <FormGroup check>
                                                                                <Label check>
                                                                                    <Input type="radio" name="radio1"
                                                                                           value="priceDesc"
                                                                                           onChange={this.sort}
                                                                                           checked={"priceDesc" == this.state.sort}
                                                                                           style={{transform: "translate(0px,1.5px)"}}/>{' '}
                                                                                    <span
                                                                                        className="fs-14 font-family-light">Narxi kamayish</span>
                                                                                </Label>
                                                                            </FormGroup>
                                                                            <FormGroup check>
                                                                                <Label check>
                                                                                    <Input type="radio" name="radio1"
                                                                                           value="countAsc"
                                                                                           onChange={this.sort}
                                                                                           checked={"countAsc" == this.state.sort}
                                                                                           style={{transform: "translate(0px,1.5px)"}}/>{' '}
                                                                                    <span
                                                                                        className="fs-14 font-family-light">Soni o'sish</span>
                                                                                </Label>
                                                                            </FormGroup>
                                                                            <FormGroup check>
                                                                                <Label check>
                                                                                    <Input type="radio" name="radio1"
                                                                                           value="countDesc"
                                                                                           onChange={this.sort}
                                                                                           checked={"countDesc" == this.state.sort}
                                                                                           style={{transform: "translate(0px,1.5px)"}}/>{' '}
                                                                                    <span
                                                                                        className="fs-14 font-family-light">Soni kamayish</span>
                                                                                </Label>
                                                                            </FormGroup>
                                                                        </Form>
                                                                    </div>
                                                                </Content>
                                                            </div>
                                                        ))}
                                                    </div>
                                                </div>
                                            </div>
                                            <div
                                                className={"d-flex reportMainSectionSub fs-12 font-family-semi-bold ml-2"}>
                                                <div onClick={() => changeCurrentOrderLinks(0)}
                                                     className={currentOrderLinks === 0 ? "mx-1 active" : "rateLink mx-1"}> Yangi
                                                </div>
                                                <div onClick={() => changeCurrentOrderLinks(1)}
                                                     className={currentOrderLinks === 1 ? "mx-1 active" : "rateLink mx-1"}> Yetkazilmoqda
                                                </div>
                                                <div onClick={() => changeCurrentOrderLinks(2)}
                                                     className={currentOrderLinks === 2 ? "mx-1 active" : "rateLink mx-1"}> Yopilgan
                                                </div>
                                                <div onClick={() => changeCurrentOrderLinks(4)}
                                                     className={currentOrderLinks === 4 ? "mx-1 active" : "rateLink mx-1"}> Qaytarilgan
                                                </div>
                                                <div onClick={() => changeCurrentOrderLinks(3)}
                                                     className={currentOrderLinks === 3 ? "mx-1 active" : "rateLink mx-1"}> Bekor
                                                    qilingan
                                                </div>
                                            </div>
                                            <div className="">
                                                <div className="d-flex position-relative h-100 align-items-center">
                                                    <div className="mx-2">
                                                        <AvForm className="search-form">
                                                            <div className="input-group">
                                                                <AvField type="text" className="form-control"
                                                                         name="searchName"
                                                                         id="search"
                                                                         onChange={this.search}
                                                                         placeholder="Qidirish"/>
                                                                <div className="input-group-append">
                                                                    <div className="">
                                                                        <div className="d-flex justify-content-end">
                                                                            <div className="">
                                                                                <button type="submit"
                                                                                        className="searchIcon"
                                                                                        onClick={() => this.search("click")}>
                                                                                    <span className="icon icon-search"/>
                                                                                </button>
                                                                            </div>
                                                                        </div>
                                                                    </div>
                                                                </div>
                                                            </div>
                                                        </AvForm>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </Col>

                    <Col md={12} className="reportMainSectionCol2">
                        <div className="adminLinksMainContent">
                            <div className="d-flex justify-content-center">
                                <div className="w-100">

                                    {currentOrderLinks === 0 ? <CurrentOrderLinksZeroComponent editOrder={editOrder}
                                                                                               orders={orders}
                                                                                               changeToPayed={changeToPayed}
                                                                                               changeOrder={changeOrder}
                                                                                               setShowHistoryFunctionState={this.setShowHistoryFunctionState}
                                                                                               setChangeOrderSumState={this.setChangeOrderSumState}
                                                                                               changeOrderSum={changeOrderSum}
                                                                                               showHistoryModal={showHistoryModal}
                                                                                               setMyState={this.setMyState}
                                                                                               pushNextModal={pushNextModal}
                                                                                               openChangeModal={openChangeModal}
                                                                                               changeCurrentEditAndSettingsLinks={changeCurrentOrderLinks}/> : ""}
                                    {currentOrderLinks === 1 ? <CurrentOrderLinksOneComponent editOrder={editOrder}
                                                                                              orders={orders}
                                                                                              changeOrder={changeOrder}
                                                                                              setShowHistoryFunctionState={this.setShowHistoryFunctionState}
                                                                                              showHistoryModal={showHistoryModal}
                                                                                              setMyState={this.setMyState}
                                                                                              pushNextModal={pushNextModal}
                                                                                              changeToPayed={changeToPayed}
                                                                                              changeCurrentEditAndSettingsLinks={changeCurrentOrderLinks}/> : ""}
                                    {currentOrderLinks === 2 ? <CurrentOrderLinksTwoComponent editOrder={editOrder}
                                                                                              orders={orders}
                                                                                              changeOrder={changeOrder}
                                                                                              setShowHistoryFunctionState={this.setShowHistoryFunctionState}
                                                                                              showHistoryModal={showHistoryModal}
                                                                                              setMyState={this.setMyState}
                                                                                              pushNextModal={pushNextModal}
                                                                                              changeToPayed={changeToPayed}
                                                                                              changeCurrentEditAndSettingsLinks={changeCurrentOrderLinks}/> : ""}
                                    {currentOrderLinks === 4 ? <CurrentOrderLinksThreeComponent editOrder={editOrder}
                                                                                                orders={orders}
                                                                                                changeOrder={changeOrder}
                                                                                                setShowHistoryFunctionState={this.setShowHistoryFunctionState}
                                                                                                showHistoryModal={showHistoryModal}
                                                                                                setMyState={this.setMyState}
                                                                                                pushNextModal={pushNextModal}
                                                                                                changeToPayed={changeToPayed}
                                                                                                changeCurrentEditAndSettingsLinks={changeCurrentOrderLinks}/> : ""}
                                    {currentOrderLinks === 3 ? <CurrentOrderLinksFourComponent editOrder={editOrder}
                                                                                               changeToPayed={changeToPayed}
                                                                                               orders={orders}
                                                                                               changeOrder={changeOrder}
                                                                                               setShowHistoryFunctionState={this.setShowHistoryFunctionState}
                                                                                               showHistoryModal={showHistoryModal}
                                                                                               setMyState={this.setMyState}
                                                                                               pushNextModal={pushNextModal}
                                                                                               changeCurrentEditAndSettingsLinks={changeCurrentOrderLinks}/> : ""}
                                </div>

                            </div>

                        </div>
                        {Math.ceil(totalElements / (this.state.activePage + 1) / 10) != 1 ?
                            <div className={totalElements ? "my-paginate mb-3" : 'd-none mb-3'}>
                                <Pagination
                                    activePage={this.state.activePage + 1}
                                    itemsCountPerPage={10}
                                    totalItemsCount={totalElements}
                                    pageRangeDisplayed={5}
                                    onChange={this.handlePageChange.bind(this)}
                                />
                            </div> : null}

                    </Col>
                </Row>

                {/*Next Modal => yani product ni keyingi bosqichga o`tishi uchun*/}
                <div className='right-modal'>
                    <Modal id="leftsite" centered={true} isOpen={nextModal} toggle={() => pushNextModal()}
                           className="h-100">
                        <ModalHeader className='position-relative text-center' toggle={() => pushNextModal()}>
                            <p className="mb-0 fs-20 font-family-bold">Buyruqni amalga oshirishga rozimisiz ?</p>
                        </ModalHeader>
                        <ModalBody className="h-100">
                            <AvForm className="formModall" onValidSubmit={this.handleSubmit}>
                                <div className="d-flex flex-column h-100">
                                    <div className="">
                                        <AvField name="message"
                                                 type="textarea"
                                                 className="w-100"
                                                 label="Ma'lumot"
                                                 placeholder="Bajarilayotgan amal haqida qisqacha ma'lumot..."
                                                 validate={
                                                     {
                                                         required: {value: true, errorMessage: "Xabarni kiriting!"}
                                                     }
                                                 }/>
                                    </div>
                                    <div className="mt-auto mb-2">
                                        <div className="d-flex justify-content-start">
                                            <div className="">
                                                <Button className="benefitBtn addBtnBenefit mr-3" type="submit"
                                                        onClick={pushNextModal}>Bajarish</Button>
                                                <Button className="benefitBtn cancelBtnBenefit"
                                                        onClick={() => pushNextModal()}>Bekor
                                                    qilish</Button>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </AvForm>
                        </ModalBody>
                    </Modal>
                </div>
                <div className='right-modal'>
                    <Modal id="leftsite" centered={true} isOpen={changeModal} toggle={() => openChangeModal()}
                           className="h-100">
                        <ModalHeader className='position-relative text-center' toggle={() => openChangeModal()}>
                            <p className="mb-0 fs-20 font-family-bold">Buyurtmani narxini kamaytirish</p>
                        </ModalHeader>
                        <ModalBody className="h-100">
                            <AvForm className="formModall" onValidSubmit={this.handleSub}>
                                <div className="d-flex flex-column h-100">
                                    <div className="">
                                        <AvField name="message"
                                                 type="textarea"
                                                 className="w-100"
                                                 label="Ma'lumot"
                                                 placeholder="Bajarilayotgan amal haqida qisqacha ma'lumot..."
                                                 validate={
                                                     {
                                                         required: {value: true, errorMessage: "Xabarni kiriting!"}
                                                     }
                                                 }/>
                                    </div>
                                    <div className="">
                                        <AvField name="price"
                                                 type="input"
                                                 className="w-100"
                                                 min={0}
                                                 label="Narxni kiriting"
                                                 placeholder="Narxni kiriting..."
                                                 validate={
                                                     {
                                                         required: {value: true, errorMessage: "Narxni kitiring!"}
                                                     }
                                                 }/>
                                    </div>
                                    <div className="">
                                        <AvField name="priceAdd"
                                                 type="checkbox"
                                                 className="w-100"
                                                 min={0}
                                                 label="Narx qo'shilsinmi?"
                                                 />
                                    </div>
                                    <div className="mt-auto mb-2">
                                        <div className="d-flex justify-content-start">
                                            <div className="">
                                                <Button className="benefitBtn addBtnBenefit mr-3" type="submit"
                                                        onClick={openChangeModal}>Bajarish</Button>
                                                <Button className="benefitBtn cancelBtnBenefit"
                                                        onClick={() => openChangeModal()}>Bekor
                                                    qilish</Button>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </AvForm>
                        </ModalBody>
                    </Modal>
                </div>
                <div className='right-modal'>
                    <Modal id="leftsite" centered={true} isOpen={historyModal} toggle={() => showHistoryModal(0)}
                           className="h-100">
                        <ModalHeader className='position-relative text-center' toggle={() => showHistoryModal(0)}>
                            <p className="mb-0 fs-20 font-family-bold">Buyurtma tarixi</p>
                        </ModalHeader>
                        <ModalBody className="h-100">
                            <div className="formModall">
                                <div className="d-flex flex-column h-100">

                                    {orders[orderIndex] ?
                                        <div className="">
                                            {orders[orderIndex].resUserOrderHistories.map((oderHistory: any) => {
                                                return <div>
                                                    <p className="fs-13 font-family-semi-bold">Foydalanuvchi
                                                        - {orders[orderIndex].resUser.fullName ? orders[orderIndex].resUser.fullName : ""}</p>
                                                    <p className="fs-13 font-family-semi-bold">Bosqich
                                                        -
                                                        {
                                                            oderHistory.currentStep === 'CREATED' ? ' Yangi!'
                                                                :
                                                                oderHistory.currentStep === 'REJECTED' ? ' Qaytarilgan!'
                                                                    :
                                                                    oderHistory.currentStep === 'CANCELLED' ? ' Bekor qilingan!'
                                                                        :
                                                                        oderHistory.currentStep === 'IN_PROGRESS' ? ' Yetkazilmoqda!'
                                                                            :
                                                                            oderHistory.currentStep === 'CLOSED' ? ' Yopilgan!'
                                                                                :
                                                                                ' To`lov amaliyoti!'
                                                        }</p>
                                                    <p className="fs-13 font-family-semi-bold">Ma`lumot
                                                        - {oderHistory.message}</p>
                                                    <p className="fs-13 font-family-semi-bold">Qachon
                                                        - {oderHistory.createdAt}</p>
                                                    {oderHistory.admin ?
                                                        <p className="fs-13 font-family-semi-bold">Admin
                                                            - {oderHistory.admin.fullName}</p> : ""}
                                                    <hr/>
                                                </div>
                                            })
                                            }
                                        </div>
                                        : ""}
                                    <div className="mt-auto mb-2">
                                        <div className="d-flex justify-content-start">
                                            <div className="">
                                                <Button className="benefitBtn cancelBtnBenefit"
                                                        onClick={() => showHistoryModal(0)}>Yopish
                                                </Button>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </ModalBody>
                    </Modal>
                </div>
                <div className='right-modal'>
                    <Modal id="leftsite" centered={true} isOpen={addModal} size='xl' toggle={() => openModal()}
                           className="h-100">

                        <ModalHeader className='position-relative text-center' toggle={() => openModal()}>
                            {!currentOrder.id ?
                                <p className="mb-0 fs-20 font-family-bold">Buyurtma qo'shish</p>
                                :
                                <p className="mb-0 fs-20 font-family-bold">Buyurtmani o'zgartirish</p>
                            }
                        </ModalHeader>
                        <ModalBody className="h-100">
                            <AvForm onValidSubmit={addOrder} className="formModall">
                                <div className="d-flex flex-column h-100">
                                    <div className="">
                                        <div className="row">
                                            <div className="col-3">
                                                <label className="Label"
                                                       style={{marginTop: "20px"}}>Mijozlar</label>
                                                {
                                                    edit ? reqCart.resUser.phoneNumber :
                                                        <SelectSearch options={usersOptions} search value="sv"
                                                                      name="users" label="Mijozlar"
                                                                      onChange={searchOnchange}
                                                                      validate={
                                                                          {
                                                                              required: {
                                                                                  value: true,
                                                                                  errorMessage: "Mijozni tanlang!"
                                                                              }
                                                                          }}
                                                                      placeholder="Mijozlar"/>
                                                }

                                            </div>
                                        </div>
                                        <div className="row mb-3">
                                            {this.state.addOrderList.map((item: any, index: any) =>
                                                <React.Fragment>
                                                    <div className="col-10">
                                                        <div className="row">
                                                            <div className="col-3">
                                                                <label className="Label"
                                                                       style={{marginTop: "20px"}}>Mahsulotlar</label>
                                                                <SelectSearch options={options} search value="sv"
                                                                              name="product"
                                                                              label="Mahsulot"
                                                                              required
                                                                              onChange={(value: any) => productChange(value, index)}
                                                                              placeholder="Mahsulotlar"/>
                                                            </div>
                                                            {/*<div className="col-3">*/}
                                                            {/*  <AvField className="modalSectionsInput" name="Details"*/}
                                                            {/*           label="O`lchov" placeholder="O`lchov"*/}
                                                            {/*           type="select"*/}
                                                            {/*           onChange={(e: any) => productPriceChange(e, index)}*/}
                                                            {/*           validate={*/}
                                                            {/*             {*/}
                                                            {/*               required: {*/}
                                                            {/*                 value: false,*/}
                                                            {/*                 errorMessage: "O`lchovni kiriting!"*/}
                                                            {/*               }*/}
                                                            {/*             }*/}
                                                            {/*           }>*/}
                                                            {/*    <option>Narx</option>*/}
                                                            {/*    {this.state.reqCart.reqSelectedProducts[index] && this.state.reqCart.reqSelectedProducts[index].selectedProduct && this.state.reqCart.reqSelectedProducts[index].selectedProduct.resProductPrices ? this.state.reqCart.reqSelectedProducts[index].selectedProduct.resProductPrices.map((res: any) => {*/}
                                                            {/*      return <option*/}
                                                            {/*        value={res.productPriceId}>{res.resQuantity.value + " " + res.resQuantity.quantityType}</option>*/}
                                                            {/*    }) : ""}*/}
                                                            {/*  </AvField>*/}
                                                            {/*</div>*/}
                                                            <div className="col-3">
                                                                <div className="d-flex align-items-center h-100">
                                                                    <div className="ml-2">
                                                                        <AvField className="modalSectionsInput"
                                                                                 name="productPrice"
                                                                                 label="Mahsulot narxi"
                                                                                 placeholder="Mahsulot narxi"
                                                                                 type="number"
                                                                                 disabled={true}
                                                                                 value={this.state.reqCart.reqSelectedProducts[index] && this.state.reqCart.reqSelectedProducts[index].selectedPrice ? this.state.reqCart.reqSelectedProducts[index].selectedPrice.price : 0}
                                                                        />
                                                                    </div>
                                                                </div>
                                                            </div>
                                                            <div className="col-3">
                                                                <AvField className="modalSectionsInput" name="count"
                                                                         label="Buyurtma miqdori"
                                                                         placeholder="buyurtma miqdori"
                                                                         type="number"
                                                                         onChange={(e: any) => countOnChange(e, index)}
                                                                         value={this.state.reqCart.reqSelectedProducts[index] ? this.state.reqCart.reqSelectedProducts[index].count : ""}
                                                                         disabled={this.state.reqCart.reqSelectedProducts[index] && this.state.reqCart.reqSelectedProducts[index].selectedPrice === undefined}
                                                                         min={0}
                                                                         validate={
                                                                             {
                                                                                 required: {
                                                                                     value: false,
                                                                                     errorMessage: "Sonini kiriting!"
                                                                                 }
                                                                             }
                                                                         }/>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </React.Fragment>
                                            )}
                                            <div className="col-2 d-flex justify-content-start"
                                                 style={{transform: "translate(0px, 23px)"}}>
                                                <div
                                                    className="d-flex justify-content-start align-items-center h-100 ml-3">
                                                    <div
                                                        onClick={() => deleteOrderFunc("item")}
                                                        className="btn btn-light ml-auto mr-3">
                                                        <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24"
                                                             width="18" height="18">
                                                            <path fill="none" d="M0 0h24v24H0z"/>
                                                            <path
                                                                d="M17 6h5v2h-2v13a1 1 0 0 1-1 1H5a1 1 0 0 1-1-1V8H2V6h5V3a1 1 0 0 1 1-1h8a1 1 0 0 1 1 1v3zm1 2H6v12h12V8zM9 4v2h6V4H9z"
                                                                fill="rgba(101,101,101,1)"/>
                                                        </svg>
                                                    </div>
                                                    <div onClick={addOrderFunc} className="btn btn-light">
                                                        <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24"
                                                             width="18" height="18">
                                                            <path fill="none" d="M0 0h24v24H0z"/>
                                                            <path
                                                                d="M11 11V7h2v4h4v2h-4v4h-2v-4H7v-2h4zm1 11C6.477 22 2 17.523 2 12S6.477 2 12 2s10 4.477 10 10-4.477 10-10 10zm0-2a8 8 0 1 0 0-16 8 8 0 0 0 0 16z"
                                                                fill="rgba(101,101,101,1)"/>
                                                        </svg>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                    <div className="mt-auto mb-2">
                                        <div className="d-flex justify-content-start">
                                            <div className="">
                                                <Button className="benefitBtn addBtnBenefit mr-3">Saqlash</Button>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </AvForm>
                        </ModalBody>
                    </Modal>
                </div>
            </div>
        );
    }
}

export default Index;
