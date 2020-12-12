import React from 'react';
import './index.less'
import '../../../global.less'
import {Button, Col, Row} from "reactstrap";
// @ts-ignore
import 'react-toastify/dist/ReactToastify.css';
import BenefitAdminLinksComponent from "@/component/BenefitAdminLinks/BenefitAdminLinksComponent";
import AddMarketing from "@/pages/admin/marketing/addMarketing/addMarketing";
import {connect} from "@@/plugin-dva/exports";
import Pagination from "react-js-pagination";
// @ts-ignore
import {AvField, AvForm} from 'availity-reactstrap-validation';
import DeleteUnversal from "@/pages/admin/marketing/delete";

interface initialState {
    [x: string]: any;

    isOpen: boolean,
    isDel: boolean,
    currentPro: any
}

// @ts-ignore

@connect(({marketing}) => ({marketing}))
class Index extends React.Component {
    state: initialState;
    props: any

    constructor(props: any) {
        super(props);
        this.state = {
            isOpen: false,
            activePage: 1,
            currentid: null,
            minDate: null,
            maxDate: null,
            filter: null,
            isCom: false,
            status: false,
            isDel: false,
            currentPro: null,
        }
    }

    handlePageChange(pageNumber: any) {
        this.setState({activePage: pageNumber});
        const {dispatch} = this.props;
        dispatch({
            type: 'marketing/getMarketing',
            payload: {
                page: pageNumber - 1,
                size: 10
            }
        })
    }


    componentDidMount(): void {
        // @ts-ignore
        const {dispatch} = this.props;
        dispatch({
            type: 'marketing/getMarketing',
            payload: {
                page: 0,
                size: 10
            }
        })
    }

    render() {
        const {marketing} = this.props;
        const {marketings, totalElements} = marketing;
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
                type: 'marketing/getMarketing',
                payload: {
                    page: this.state.activePage - 1,
                    size: 10
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
                                            Reklama <br/> qo'shish
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
                                            <th scope="col" className="text-left fs-11 font-family-bold pl-2">Rasm</th>
                                            <th scope="col"
                                                className="text-left fs-11 font-family-bold">SARLAVHA(UZBEK)
                                            </th>
                                            <th scope="col" className="text-left fs-11 font-family-bold">IZOH(UZBEK)
                                            </th>
                                            <th scope="col" className="text-left fs-11 font-family-bold">AMAL</th>
                                            <th scope="col" className="text-left fs-11 font-family-bold"></th>
                                        </tr>
                                        </thead>
                                        <tbody className="app-information2-user">
                                        {marketings.length ? marketings.map((sale: { files: { fileId: string; }[]; titleUz: React.ReactNode; titleRu: React.ReactNode; textUz: React.ReactNode; textRu: React.ReactNode; id: any; }, inx: string | number | undefined) =>
                                            <tr className="all-hover-tr boxShadowCards pointer" key={inx}>
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
                                                        <div
                                                            className="edit-tool-con text-left filter-design w-max position-absolute py-3 px-1">
                                                            <div className="triangle"/>
                                                            <div className="container">
                                                                {/*Filter Navbar*/}
                                                                <div className="row">
                                                                    <div
                                                                        className="col-12 pl-0 mb-3">
                                                                        <img src={"/api/file/" + sale.photoId}
                                                                             alt="" className='img-fluid'/></div>
                                                                </div>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </th>
                                                <th className="pl-2">
                                                    <h6>{sale.titleUz}</h6>
                                                </th>
                                                <th>
                                                    <h6>{sale.textUz}</h6>
                                                </th>
                                                <th>
                                                    {/*<div className="tbody-div d-flex justify-content-start">*/}
                                                    {/*    <div className="change-order-btn btn-size-md">*/}
                                                    {/*        <button onClick={() => toggle(sale)}>*/}
                                                    {/*            <span className="icon icon-edit"/>*/}
                                                    {/*        </button>*/}
                                                    {/*    </div>*/}
                                                    <div className="change-order-btn btn-size-md ml-3">
                                                        <button onClick={() => del(sale.id)}>
                                                            <span className="icon icon-rubbish"/>
                                                        </button>
                                                    </div>
                                                    {/*</div>*/}
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
                {marketings ?
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
                    <AddMarketing value={{
                        isOpen: this.state.isOpen,
                        toggle,
                        current: this.state.currentPro ? this.state.currentPro : null
                    }}/>
                    : ''}
                {this.state.isDel ?
                    <DeleteUnversal value={{isOpen: this.state.isDel, toggle: del, currentId: this.state.currentid}}/>
                    : ''}
            </div>
        );
    }
}

export default Index;
