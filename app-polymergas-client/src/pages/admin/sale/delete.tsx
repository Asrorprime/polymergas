import React, {Component} from 'react';
import {Button, Modal, ModalBody, ModalFooter, ModalHeader, Row, Col} from 'reactstrap';
import {connect} from "react-redux";
// @ts-ignore
import {AvField, AvForm} from 'availity-reactstrap-validation';

// @ts-ignore
@connect(({sale}) => ({sale}))
class DeleteUnversal extends Component {

  render() {
    const props = this.props;
    // @ts-ignore
    const {className, dispatch} = props;
    const deleteSale = () => {
      // @ts-ignore
      const {dispatch} = this.props;
      dispatch({
        type: 'sale/deleteSale',
        payload: {
          id: this.props.value.currentId
        }
      });
      this.props.value.toggle()
    };
    return (
      <div className='right-modal'>
        <Modal id="leftsite" size='md' centered={true} isOpen={props.value.isOpen} className={className}
               toggle={this.props.value.toggle}>
          <ModalHeader className='position-relative'
                       toggle={this.props.value.toggle}>
            <p className="mb-0 fs-16 font-family-semi-bold">Aksiyani o`chirish</p>
          </ModalHeader>
          <ModalBody>
            <AvForm className="formModall">
              <div className="d-flex flex-column h-100">
                <div className="">
                  <p className="mb-0 fs-16 font-family-medium text-center">Siz chindan ham bu aksiyani o'chirmoqchimisiz
                    ?</p>
                  <p className="mb-0 fs-14 font-family-medium text-center" style={{color: "#61676d"}}>(Ushbu maxsulotini
                    ni o`chirganingizdan keyin qaytarib bo`lmaydi.)</p>
                </div>
                <div className="mt-auto mb-2">
                  <div className="d-flex justify-content-start">
                    <div className="">
                      <Button className="deleteBtnBenefit benefitBtn mr-3" onClick={deleteSale}>O`chirish</Button>
                      <Button className="benefitBtn cancelBtnBenefit" onClick={this.props.value.toggle}>Bekor
                        qilish</Button>
                    </div>
                  </div>
                </div>
              </div>
            </AvForm>
          </ModalBody>
        </Modal>
      </div>
    )
  }
}

export default DeleteUnversal;
