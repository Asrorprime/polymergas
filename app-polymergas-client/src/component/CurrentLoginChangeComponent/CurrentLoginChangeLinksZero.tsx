import React from 'react';
import {Button, Col, Modal, ModalBody, ModalHeader, Row} from "reactstrap";

interface initialState {

}
// @ts-ignore
import {AvField, AvForm} from "availity-reactstrap-validation";
import {connect} from "dva";
import app from "@/models/app";

interface initialState {
  password: string,
}

// @ts-ignore
@connect(({app})=>({app}))
class CurrentLoginChangeLinksZero extends React.Component {
  state: initialState;
  props: any

  constructor(props: any) {
    super(props);
    this.state = {
      password: '',
    }
    this.handlePasswordChange = this.handlePasswordChange.bind(this);
  }

  handlePasswordChange(e: { target: { value: any; }; }) {
    this.setState({password: e.target.value});
  }

  componentDidMount() {
    // @ts-ignore
    if (this.props.password) {
      // @ts-ignore
      this.setState({password: this.props.password});
    }
  }

  render() {
    const {dispatch,changeCurrentEditAndSettingsLinks} = this.props;

    const handleSubmit = (e: { preventDefault: () => void; }, v: any) => {
      e.preventDefault();
      dispatch({
        type: 'login/editPassword',
        payload: {
          ...v
        }
      })
      // then((res:any)=>{
      //   changeCurrentEditAndSettingsLinks(1)
      // });
    };
    const updateState = (e: { target: { name: any; value: any; }; }) => {
      dispatch({
        type: 'login/updateState',
        payload: {
          [e.target.name]: e.target.value
        }
      })
    };
    return (
      <div className="CurrentLoginChangeLinksZero">
        <AvForm onValidSubmit={handleSubmit} className="formModall">
          <div className="d-flex flex-column h-100">
            <div className="">
              <AvField type="password" className="modalSectionsInput" label="Eski parol" onChange={updateState}
                       name="oldPassword"
                       placeholder="******"
                       validate={{
                         required: {value: true, errorMessage: 'Eski parolni kiritish shart!'},

                       }}/>
              <AvField type="password" className="modalSectionsInput" label="Yangi parol" name="password"
                       placeholder="******" validate={
                {
                  required: {value: true, errorMessage: "Yangi parolni kiritish shart!"},
                }
              }/>
              <AvField type="password" className="modalSectionsInput" label="Yangi parolni tasdiqlash"
                       name="prePassword"
                       placeholder="******" validate={
                {
                  required: {value: true, errorMessage: "Yangi parolni tasdiqlash shart!"},
                }
              }/>
            </div>
            <div className="mt-auto mb-4">
              <div className="d-flex justify-content-start">
                <div className="">
                  <Button type="submit" className="benefitBtn addBtnBenefit mr-3">Saqlash</Button>
                </div>
              </div>
            </div>
          </div>
        </AvForm>
      </div>
    );
  }
}

export default CurrentLoginChangeLinksZero;
