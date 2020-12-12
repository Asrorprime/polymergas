import React, {Component} from 'react';
import './index.less'
import {Button, Card, CardBody, CardFooter, CardHeader, Col} from "reactstrap";
// @ts-ignore
import {AvField, AvForm, AvGroup} from 'availity-reactstrap-validation';
import {connect} from "dva";
import {FaEye, FaEyeSlash} from "react-icons/fa";

interface initialState {
  type: string;
}

// @ts-ignore
@connect(({app}) => ({app}))
class Login extends Component {

  state: initialState;
  props: any;

  constructor(props: any) {
    super(props);
    this.state = {
      type: 'password'
    }
    this.showHide = this.showHide.bind(this);
  }

  showHide(e: any) {
    this.setState({
      type: this.state.type === 'password' ? 'input' : 'password'
    })
  }

  // componentWillReceiveProps(nextProps: any) {
  //   this.setState({
  //     productList: nextProps.products.list
  //   });
  // }
  refresh(value: any) {
    const {dispatch} = this.props;
    dispatch({
      type: 'products/getProducts',
      payload: {},
    });
  }

  render() {

    const {dispatch} = this.props;
    const loginUser = (e: any, v: any) => {
      dispatch({
        type: 'app/signIn',
        payload: {
          ...v
        }
      }).then((enable: any) => {
          if (enable) {
            dispatch({
              type: 'app/userMe'
            })
          }

        }
      )
    };


    return (
      <div className='vh-100 d-flex login-page'>
        <div className="w-50">
          <div className="loginLogoBrand">
            <div className="d-flex align-items-center justify-content-center h-100 w-100">
              <div className="text-center">
                <h2>Polymergas</h2>
                {/*<img className="img-fluid" src="/assets/icon/benefit_logo.svg" alt=""/>*/}
              </div>
            </div>
          </div>
        </div>
        <div className="w-50 d-flex justify-content-center vh-100 align-items-center">
          <Col md={8}>
            <Card>
              <CardHeader className="text-left">
                <h4 className="font-family-bold main-text-color fs-25 lh-16 py-3 m-0">
                  Tizimga xush kelibsiz !
                </h4>
                <p className="fs-16 lh-22 text-black-50 font-family-medium">Tizimga kirish
                  uchun, login va parol orqali
                  autentifikatsiya jarayonidan o’ting</p>
              </CardHeader>
              <AvForm onValidSubmit={loginUser}>
                <CardBody>
                  <div className="branch-line"/>
                  <h6 className="branch-title">Polymergas</h6>
                  <div className=""  style={{borderTop: "0.1px solid rgba(151, 151, 151, 0.2)"}}>
                    <AvField className="pr-2" name={'phoneNumber'} type={'text'} placeholder={'Telefon raqamni kiriting'}
                             validate={{
                               required: {value: true, errorMessage: 'Telefon raqam kiritilmadi'},
                               // pattern: {
                               //   value: '^(?=[a-zA-Z0-9._]{3,20}$)(?!.*[_.]{2})[^_.].*[^_.]$',
                               //   errorMessage: 'Foydalanuvchi nomi 3 tadan kam bulmasligi, maxsus belgilar qatnashmasligi shart! '
                               // }
                             }}/>
                  </div>
                  <AvGroup className="w-100">
                    <div className="w-100">
                      <div className="d-flex align-items-center h-100 w-100" style={{borderTop: "0.1px solid rgba(151, 151, 151, 0.2)"}}>
                        <div className="w-100">
                          <AvField type={this.state.type} name="password" placeholder="Parolni kiriting" className="w-100"
                                   validate={{
                                     required: {value: true, errorMessage: 'Parol kiritilmadi'},
                                     // pattern: {
                                     //   value: '[a-zA-Z0-9]{6,30}',
                                     //   errorMessage: 'Parol eng kamida 6 ta belgidan iborat boʻlishi kerak'
                                     // }
                                   }}/>
                        </div>
                        <div className="px-2">
                          <div className="password__show" onClick={this.showHide}>{this.state.type === 'input' ?
                            <FaEye/> : <FaEyeSlash/>}
                          </div>
                        </div>
                      </div>
                    </div>
                  </AvGroup>
                </CardBody>
                <CardFooter>
                  <p className="text-right m-0 pt-0">
                    <Button color="outline-info" className="btn-block">
                      Tizimga kirish
                    </Button>
                  </p>
                </CardFooter>
              </AvForm>
            </Card>
          </Col>
        </div>
      </div>
    );
  }
}

export default (Login);



