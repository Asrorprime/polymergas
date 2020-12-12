import React, {Component} from 'react';
import {Button, Col, Modal, ModalBody, ModalFooter, ModalHeader, Row} from 'reactstrap';
import {connect} from "react-redux";

// @ts-ignore
@connect(({app}) => ({app}))
class Comfirm extends Component {

  render() {
    const props = this.props;
    // @ts-ignore
    const {className, dispatch} = props;
    const deleteWhereHouse = () => {
      // @ts-ignore
      const {dispatch} = this.props;
      dispatch({
        type: 'app/switchToNew',
        payload: {
          path: this.props.value.currentId,
          isNew: this.props.value.status
        }
      });
      this.props.value.toggle('success')
    };
    return (
      <div className='right-modal'>
        <Modal id="leftsite" size='md' centered={true} isOpen={props.value.isOpen} className={className}
               toggle={this.props.value.toggle}>
          <ModalHeader className=' font-s-20 position-relative mt-2 pl-4 text-center'
                       toggle={this.props.value.toggle}>Mahsulotni yangilash
          </ModalHeader>
          <ModalBody>
            <Row className='mx-5 text-center'>
              <Col md={12}>
                <p className='text-center'>
                  Siz chindan ham bu mahsulotni yangi mahsulot safiga qoshmoqchimisiz
                </p>
              </Col>
            </Row>
          </ModalBody>
          <ModalFooter className='d-inline   border-0'>
            <Button color="primary" className='py-1' type="submit" style={{transform: 'translate(12px)'}}
                    onClick={deleteWhereHouse}>Tasdiqlash</Button>
          </ModalFooter>

        </Modal>
      </div>
    )
  }
}

export default Comfirm;
