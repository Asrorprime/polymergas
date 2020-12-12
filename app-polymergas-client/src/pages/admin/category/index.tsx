import React from 'react';

import '../../../global.less'
import {Button, Col, Row} from "reactstrap";
// @ts-ignore
import 'react-toastify/dist/ReactToastify.css';
import BenefitAdminLinksComponent from "@/component/BenefitAdminLinks/BenefitAdminLinksComponent";
import DeleteUnversal from "@/pages/admin/category/delete";
import AddNew from "@/pages/admin/category/AddCategory";
import Pagination from "react-js-pagination";
import {connect} from "@@/plugin-dva/exports";
// @ts-ignore
import {AvField, AvForm} from 'availity-reactstrap-validation';
import AddCategory from "@/pages/admin/category/AddCategory";


interface initialState {
  [x: string]: any;
  isOpen: boolean,
  isDel: boolean,
  currentCategory: any,
  nameUz: string,
  nameRu: string,
  nameEn: string,
}

// @ts-ignore
@connect(({category, app}) => ({category, app}))
class Index extends React.Component {
  state: initialState;
  props: any

  constructor(props: any) {
    super(props);
    this.state = {
      isOpen: false,
      isDel: false,
      currentCategory: null,
      nameUz: "",
      nameRu: "",
      nameEn: ""
    }
  }
  handlePageChange(pageNumber: any) {
    this.setState({activePage: pageNumber});
    const {dispatch} = this.props;
    dispatch({
      type: 'category/getCategories',
      payload: {
        page: pageNumber-1,
        size: 10
      }
    })
  }

  componentDidMount(): void {
    // @ts-ignore
    const {dispatch} = this.props;
    dispatch({
      type: 'category/getCategories',
      payload: {
        page: 0,
        size: 10
      }
    })
  }



  render() {
    // @ts-ignore
    const {dispatch} = this.props;
    // @ts-ignore
    const {categories, totalElements, searchedCategories} = this.props.category;
    const del = (currentMainColor: any) => {
      // @ts-ignore
      this.setState({isDel: !this.state.isDel, currentId: currentMainColor})
    };

    const toggle = (currentCategory: any) => {
      // @ts-ignore
      this.setState({isOpen: !this.state.isOpen, currentCategory: currentCategory})
    };


    const setNames = (e: { target: { value: string; }; }) => {
      this.state.nameUz = e.target.value
      this.state.nameRu = e.target.value
      this.state.nameEn = e.target.value

      this.setState(this.state)
    }

    const search = (e: any, v: any) => {
        dispatch({
          type: "category/getCategories",
          payload: {
            ...v,
            page: 0,
            size: 10
          }
        })
    };

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
                      Kategoriya <br/> qo'shish
                    </p>
                  </div>
                </div>
                <div className="col-lg-10 d-none-mb h-88">
                  <div className="d-flex align-items-center h-100 ml-3">
                    <AvForm onValidSubmit={search} className="search-form">
                      <div className="input-group">
                        <AvField onChange={setNames} type="text" className="form-control" name="searchName"
                                 id="search"
                                 placeholder="Qidirish"/>
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
                      <th scope="col" className="text-left fs-11 font-family-bold pl-2">NOMI UZ</th>
                      <th scope="col" className="text-left fs-11 font-family-bold">NOMI RU</th>
                      <th scope="col" className="text-left fs-11 font-family-bold">NOMI EN</th>
                      <th scope="col" className="text-left fs-11 font-family-bold">AMAL</th>
                      <th scope="col" className="text-left fs-11 font-family-bold"/>
                    </tr>
                    </thead>
                    <tbody className="app-information2-user">
                    {searchedCategories.length > 0 && searchedCategories !== '404' ?
                      searchedCategories.map((category: {
                      nameUz: React.ReactNode;
                      nameRu: React.ReactNode;
                      nameEn: React.ReactNode;
                      id: any; }, index: string | number | undefined) => {
                        return (
                          <tr className="all-hover-tr boxShadowCards pointer" key={index}>
                            <th className="pl-2">
                              <h6>{category.nameUz}</h6>
                            </th>
                            <th>
                              <h6>{category.nameRu}</h6>
                            </th>
                            <th>
                              <h6>{category.nameEn}</h6>
                            </th>
                            <th>
                              <div className="tbody-div d-flex justify-content-start">
                                <div className="change-order-btn btn-size-md">
                                  <button onClick={() => toggle(category)}>
                                    <span className="icon icon-edit"/>
                                  </button>
                                </div>
                                <div className="change-order-btn btn-size-md ml-3">
                                  <button onClick={() => del(category.categoryId)}>
                                    <span className="icon icon-rubbish"/>
                                  </button>
                                </div>
                              </div>
                            </th>
                          </tr>
                        )
                      }) : searchedCategories === '404' ?
                        <tr className="boxShadowCards">
                          <th colSpan={12}>
                            <h6 className="mb-0 ">Ma'lumot mavjud emas !</h6>
                          </th>
                        </tr> :
                            categories[0]? categories.map((category: {
                        nameUz: React.ReactNode;
                        nameRu: React.ReactNode;
                        nameEn: React.ReactNode;
                        id: any; }, index: string | number | undefined) =>
                      <tr className="all-hover-tr boxShadowCards pointer" key={index}>
                        <th className="pl-2">
                          <h6>{category.nameUz}</h6>
                        </th>
                        <th>
                          <h6>{category.nameRu}</h6>
                        </th>
                        <th>
                          <h6>{category.nameEn}</h6>
                        </th>
                        <th>
                          <div className="tbody-div d-flex justify-content-start">
                            <div className="change-order-btn btn-size-md">
                              <button onClick={() => toggle(category)}>
                                <span className="icon icon-edit"/>
                              </button>
                            </div>
                            <div className="change-order-btn btn-size-md ml-3">
                              <button onClick={() => del(category.categoryId)}>
                                <span className="icon icon-rubbish"/>
                              </button>
                            </div>
                          </div>
                        </th>
                      </tr>)
                          : <tr className="boxShadowCards">
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
        {categories[0] ?
          <div className={totalElements?"my-paginate mb-3":'d-none mb-3' }>
            <Pagination
              activePage={this.state.activePage}
              itemsCountPerPage={10}
              totalItemsCount={totalElements ? totalElements : ''}
              pageRangeDisplayed={5}
              onChange={this.handlePageChange.bind(this)}
            />
          </div>:null}
        {this.state.isOpen ?
          <AddCategory value={{isOpen: this.state.isOpen, toggle,current: this.state.currentCategory? this.state.currentCategory:null}}/>
          : ''}
        {this.state.isDel ?
          <DeleteUnversal value={{isOpen: this.state.isDel, currentId: this.state.currentId,toggle: del}}/>
          : ''}
      </div>
    );
  }
}

export default Index;
