import React from 'react';
import {Button, Col, Modal, ModalBody, ModalHeader, Row} from "reactstrap";

interface initialState {

}
// @ts-ignore
import {AvField, AvForm} from "availity-reactstrap-validation";
import {connect} from "dva";
import CurrentLoginChangesLinksMain
  from "@/component/CurrentLoginChangeComponent/CurrentLoginChangeMain/CurrentLoginChangeMainComponent";

interface initialState {

  userName: string,
}

// @ts-ignore
@connect(({app})=>({app}))
class CurrentLoginChangeLinksOne extends React.Component {
  state: initialState;
  props: any

  constructor(props: any) {
    super(props);
    this.state = {

      userName: "",
    }
    this.handleUserNameChange = this.handleUserNameChange.bind(this);
  }
  handleUserNameChange(e: { target: { value: any; }; }) {
    this.setState({userName: e.target.value});
  }

  componentDidMount() {

    // @ts-ignore
    if (this.props.userName) {
      // @ts-ignore
      this.setState({userName: this.props.userName});
    }
  }

  render() {
    const {dispatch} = this.props;

    const handleSubmit2 = (e: { preventDefault: () => void; }, v: any) => {
      dispatch({
        type: 'login/editUserName',
        payload: {
          ...v
        }
      });
    };
    const updateState2 = (e: { target: { name: any; value: any; }; }) => {
      dispatch({
        type: 'login/updateState',
        payload: {
          [e.target.name]: e.target.value
        }
      })
    };
    return (
      <div className="CurrentLoginChangeLinksOne">
        <AvForm onValidSubmit={handleSubmit2} className="formModall">
          <div className="d-flex flex-column h-100">
            <div className="">
              <AvField type="text" className="modalSectionsInput" label="Foydalanuvchi nomi"
                       onChange={updateState2} name="phoneNumber"
                       placeholder="Eski telefon raqam nomi" validate={
                {
                  required: {value: true, errorMessage: "Foydalanuvchi nomini kiritish shart!"},

                }
              }/>
              <AvField type="text" className="modalSectionsInput" label="Yangi telefon raqam"
                       name="newPhoneNumber"
                       placeholder="Yangi telefon raqamni tasdiqlash" validate={
                {
                  required: {value: true, errorMessage: "Yangi telefon raqam kiritish shart!"},

                }
              }/>
              <AvField type="password" className="modalSectionsInput" label="Parol" name="oldPassword"
                       placeholder="******" validate={
                {
                  required: {value: true, errorMessage: "Parolni kiritish shart!"},

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

export default CurrentLoginChangeLinksOne;
