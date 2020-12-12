import React, {Component} from 'react';
import {Button, Modal, ModalBody, ModalHeader} from 'reactstrap';
import {connect} from "react-redux";

interface initialState {
}


// @ts-ignore
@connect(({productType}) => ({productType}))
class DeleteUnversal extends Component {
    state: initialState;
    props: any

    constructor(props: any) {
        super(props);
        this.state = {}
    }

    render() {
        const props = this.props;
        // @ts-ignore
        const {className} = props;
        const deleteWhereHouse = () => {
            // @ts-ignore
            const {dispatch} = this.props;
            dispatch({
                type: 'productType/deleteProductType',
                payload: {
                    id: this.props.value.currentId
                }
            });
            this.props.value.toggle()
        };
        return (
            <div className='right-modal'>
                <Modal id="leftsite" centered={true} isOpen={props.value.isOpen} className={className}
                       toggle={this.props.value.toggle}>
                    <ModalHeader className='position-relative'
                                 toggle={this.props.value.toggle}>
                        <p className="mb-0 fs-16 font-family-semi-bold">Mahsulot tipini o`chirish</p>
                    </ModalHeader>
                    <ModalBody>
                        <div className="formModall">
                            <div className="d-flex flex-column h-100">
                                <div className="">
                                    <p className="mb-0 fs-16 font-family-medium text-center">Siz chindan ham
                                        mahsulot tipini o`chirmoqchimisiz ?</p>
                                    <p className="mb-0 fs-14 font-family-medium text-center"
                                       style={{color: "#61676d"}}>(Ushbu mahsulot tipini o`chirganingizdan keyin qaytarib
                                        bo`lmaydi.)</p>
                                </div>
                                <div className="mt-auto mb-2">
                                    <div className="d-flex justify-content-start">
                                        <div className="">
                                            <Button className="deleteBtnBenefit benefitBtn mr-3" type="submit"
                                                    onClick={deleteWhereHouse}>O`chirish</Button>
                                            <Button className="benefitBtn cancelBtnBenefit"
                                                    onClick={() => this.props.value.toggle(null)}>Bekor qilish</Button>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </ModalBody>
                </Modal>
            </div>
        )
    }
}

export default DeleteUnversal;
