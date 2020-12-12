import React from "react";
import {Link} from "react-router-dom";
import './index.less'
import '../../global.less'
// @ts-ignore
import {connect, history} from 'umi';
import {Modal, ModalBody, ModalHeader} from "reactstrap";
// @ts-ignore
import CurrentLoginChangesLinksMain
  from "@/component/CurrentLoginChangeComponent/CurrentLoginChangeMain/CurrentLoginChangeMainComponent";

interface initialState {
  menu: {
    name: JSX.Element | any,
    link: string | undefined,
    icon: string,
  }[],
  modal: boolean,
  modal2: boolean,
  active: any,
  // userName: string,
  // password: string,
}

// @ts-ignore
@connect(({login, app}) => ({login, app}))
class BenefitAdminLinksComponent extends React.Component {

  state: initialState;

  constructor(props: any) {
    super(props);
    this.state = {
      active: "",
      // password: '',
      menu: [
        {
          name: "Dashboard",
          link: "/admin/dashboard",
          icon: "icon icon-layers"
        },
        {
          name: "Buyurtma",
          link: "/admin/order",
          icon: "icon icon-truck-line"
        },
        {
          name: "Kategoriyalar",
          link: "/admin/category",
          icon: "icon icon-katalog"
        },
        {
          name: "Mahsulotlar",
          link: "/admin/products",
          icon: "icon icon-shopping-cart"
        },
        {
          name: "Mahsulot tipi",
          link: "/admin/productType",
          icon: "icon icon-layers"
        },
        {
          name: "Aksiya",
          link: "/admin/sale",
          icon: "icon icon-funds-line"
        },
        {
          name: "Reklama",
          link: "/admin/marketing",
          icon: "icon icon-layers"
        },
        {
          name: "Foydalanuvchilar",
          link: "/admin/exploiter",
          icon: "icon icon-user-6-line"
        },
        {
          name: <span onClick={this.toggleModalTag2}>Profilni tahrirlash</span>,
          link: undefined,
          icon: "icon icon-key-2-line"
        },
      ],
      // userName: "",
      modal: false,
      modal2: false,
    };
  }


  componentDidMount() {

    // @ts-ignore
    const pathName = this.props.app.locationPathname;

    if (pathName === "/admin/dashboard") {
      this.changeActive({i: 0});
    } else if (pathName === "/admin/order") {
      this.changeActive({i: 1});
    } else if (pathName === "/admin/category") {
      this.changeActive({i: 2});
    } else if (pathName === "/admin/products") {
      this.changeActive({i: 3});
    } else if (pathName === "/admin/productType") {
      this.changeActive({i: 4});
    } else if (pathName === "/admin/sale") {
      this.changeActive({i: 5});
    } else if (pathName === "/admin/marketing") {
      this.changeActive({i: 6});
    } else if (pathName === "/admin/exploiter") {
      this.changeActive({i: 7});
    } else {
      this.changeActive({i: 0});
    }
  }

  changeActive = ({i}: { i: number }) => {
    this.setState({
      active: i
    });
  };

  logOut = () => {
    localStorage.removeItem("polymergas-token");
    history.push("/");
  };
  openNav = () => {
    // @ts-ignore
    document.getElementById("myNav").style.width = "20%";
    // @ts-ignore
    document.getElementById("headerMyNav").style.width = "100%";
    // @ts-ignore
    // document.getElementById("brandSvg").style.width = "100px";
  };
  closeNav = () => {
    // @ts-ignore
    document.getElementById("myNav").style.width = "0%";
    // @ts-ignore
    document.getElementById("headerMyNav").style.width = "0%";
    // @ts-ignore
    // document.getElementById("brandSvg").style.width = "0px";
  };

  toggleModalTag = (tab: any) => {
    this.state.modal = !this.state.modal;
    this.setState(this.state)
  };
  toggleModalTag2 = (tab: any) => {
    this.state.modal2 = !this.state.modal2;
    this.setState(this.state)
  };

  render() {

    // @ts-ignore
    const {app, dispatch, login} = this.props;
    const {admin, currentUser} = app;
    // const handleSubmit = (e: { preventDefault: () => void; }, v: any) => {
    //   e.preventDefault();
    //   dispatch({
    //     type: 'login/editPassword',
    //     payload: {
    //       ...v
    //     }
    //   });
    //   this.state.modal = !this.state.modal
    // };
    // const handleSubmit2 = (e: { preventDefault: () => void; }, v: any) => {
    //   dispatch({
    //     type: 'login/editUserName',
    //     payload: {
    //       ...v
    //     }
    //   });
    //   this.state.modal2 = !this.state.modal2
    // };
    // const updateState = (e: { target: { name: any; value: any; }; }) => {
    //   dispatch({
    //     type: 'login/updateState',
    //     payload: {
    //       [e.target.name]: e.target.value
    //     }
    //   })
    // };
    // const updateState2 = (e: { target: { name: any; value: any; }; }) => {
    //   dispatch({
    //     type: 'login/updateState',
    //     payload: {
    //       [e.target.name]: e.target.value
    //     }
    //   })
    // };
    const items = this.state.menu.map((item, i: number) => {
      return (
        <li key={i} onClick={() => this.changeActive({i})}>
          {item.link != undefined ? <Link to={item.link}
                                          className={"d-flex mt-2 align-items-center text-decoration-none " + (this.state.active === i ? "active" : "")}>
            <div className="pl-4">
              <div className="badge-icon">
                <span className={item.icon}/>
              </div>
            </div>
            <div className="px-3 fs-15 lh-18 font-family-medium" style={{paddingTop: ".8rem", paddingBottom: ".8rem"}}>
              {item.name}
            </div>
          </Link> : <p
            className={"linkk d-flex mt-2 align-items-center text-decoration-none " + (this.state.active === i ? "linkk active" : "")}
            style={{cursor: "pointer"}}>
            <div className="pl-4">
              <div className="badge-icon">
                <span className={item.icon}/>
              </div>
            </div>
            <div className="px-3 fs-15 lh-18 font-family-medium" style={{paddingTop: ".8rem", paddingBottom: ".8rem"}}>
              {item.name}
            </div>
          </p>}
        </li>
      )
    });

    return (
      <div className="">
        <div className="change-dashboard" onClick={this.openNav}>
          <span className="icon icon-settings"/>
        </div>
        <div id="headerMyNav" className="overlay2" onClick={this.closeNav}/>
        <div id="myNav" className="overlay">
          <div className="">
            {/*<div className="">*/}
            {/*  <img id="brandSvg" className="brandSvgClass" src="/assets/icon/benefit_logo.svg" alt=""/>*/}
            {/*</div>*/}
            <div className="">
              <a href="javascript:void(0)" className="closebtn text-decoration-none" onClick={this.closeNav}>&times;</a>
            </div>
          </div>
          <div className="overlay-content">
            <div className="benefitAdminLinks">
              <ul className="list-unstyled app-menu">
                {items}
                <li onClick={this.logOut}>
                  <Link to="/" className={"mt-2 d-flex align-items-center text-decoration-none"}>
                    <div className="py-1 pl-4">
                      <div className="badge-icon">
                        <span className="icon icon-logout"/>
                      </div>
                    </div>
                    <div className="py-1 px-3 fs-15 lh-18 font-family-medium">
                      Chiqish
                    </div>
                  </Link>
                </li>
              </ul>
            </div>
          </div>
        </div>


        {/*<div className='right-modal'>*/}
        {/*  <Modal id="leftsite" centered={true} isOpen={this.state.modal} toggle={this.toggleModalTag} className="h-100">*/}
        {/*    <ModalHeader className='position-relative text-center' toggle={this.toggleModalTag}>*/}
        {/*      <p className="mb-0 fs-20 font-family-bold">Parol ni o`zgartirish</p>*/}
        {/*    </ModalHeader>*/}
        {/*    <ModalBody className="h-100">*/}
        {/*      <AvForm onValidSubmit={handleSubmit} className="formModall">*/}
        {/*        <div className="d-flex flex-column h-100">*/}
        {/*          <div className="">*/}
        {/*            <AvField type="password" className="modalSectionsInput" label="Eski parol" onChange={updateState}*/}
        {/*                     name="oldPassword"*/}
        {/*                     placeholder="******" validate={*/}
        {/*              {*/}
        {/*                required: {value: true, errorMessage: "Eski parolni kiritish shart!"}*/}
        {/*              }*/}
        {/*            }/>*/}
        {/*            <AvField type="password" className="modalSectionsInput" label="Yangi parol" name="password"*/}
        {/*                     placeholder="******" validate={*/}
        {/*              {*/}
        {/*                required: {value: true, errorMessage: "Yangi parolni kiritish shart!"}*/}
        {/*              }*/}
        {/*            }/>*/}
        {/*            <AvField type="password" className="modalSectionsInput" label="Yangi parolni tasdiqlash"*/}
        {/*                     name="prePassword"*/}
        {/*                     placeholder="******" validate={*/}
        {/*              {*/}
        {/*                required: {value: true, errorMessage: "Yangi parolni tasdiqlash shart!"}*/}
        {/*              }*/}
        {/*            }/>*/}
        {/*          </div>*/}
        {/*          <div className="mt-auto mb-2">*/}
        {/*            <div className="d-flex justify-content-start">*/}
        {/*              <div className="">*/}
        {/*                <Button type="submit" className="benefitBtn addBtnBenefit mr-3">Saqlash</Button>*/}
        {/*              </div>*/}
        {/*            </div>*/}
        {/*          </div>*/}
        {/*        </div>*/}
        {/*      </AvForm>*/}
        {/*    </ModalBody>*/}
        {/*  </Modal>*/}
        {/*</div>*/}

        <div className='right-modal'>
          <Modal id="leftsite" centered={true} isOpen={this.state.modal2} toggle={this.toggleModalTag2}
                 className="">
            <ModalHeader className='position-relative text-center' toggle={this.toggleModalTag2}>
              <p className="mb-0 fs-20 font-family-bold">Login va Parol ni o`zgartirish</p>
            </ModalHeader>

            <ModalBody className="h-100">
              <CurrentLoginChangesLinksMain/>
            </ModalBody>
          </Modal>
        </div>
      </div>
    )
  }
}

export default BenefitAdminLinksComponent;
