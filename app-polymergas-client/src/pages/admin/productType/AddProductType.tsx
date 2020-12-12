import React, {Component} from 'react';
import {Button, Modal, ModalBody, ModalHeader} from 'reactstrap';
import {connect} from "@@/plugin-dva/exports";
// @ts-ignore
import {AvField, AvForm} from 'availity-reactstrap-validation';

// @ts-ignore
@connect(({app}) => ({app}))
class AddProductType extends Component {
    props: any;

    render() {
        const props = this.props;
        // @ts-ignore
        const {
            className, dispatch
        } = props;
        const {current} = props.value;
        const saveProductType = (a: any, v: any) => {
            dispatch({
                type: "productType/saveProductType",
                payload: {
                    // id: edit.id ? edit.id : null,
                    ...v,
                  productTypeId: current.productTypeId ? current.productTypeId : null
                }
            }).then((res: any) =>
                dispatch({
                    type: 'productType/getProductTypes',
                    payload: {
                        page: 0,
                        size: 10
                    }
                }));
            this.props.value.toggle(null)
        };
        return (
            <div className='right-modal'>
                <Modal id="leftsite" centered={true}  isOpen={props.value.isOpen} className={className}>
                    <ModalHeader className='position-relative text-center' toggle={() => this.props.value.toggle(null)}>
                        {!current.productTypeId ?
                            <p className="mb-0 fs-16 font-family-semi-bold">Mahsulot tipi qo’shish</p>
                            :
                            <p className="mb-0 fs-16 font-family-semi-bold">Mahsulot tipini o'zgartirish</p>
                        }
                    </ModalHeader>
                    <ModalBody>
                        <AvForm onValidSubmit={saveProductType} className="formModall">
                            <div className="d-flex flex-column h-100">
                                <div className="">
                                    <div className="row mt-3">
                                        <div className="col-12">
                                            <AvField defaultValue={current.productTypeId ? current.nameUz : null}
                                                     label="Nomi (uz)"
                                                     name="nameUz" placeholder="O'zbekcha nomi" required validate={
                                                {
                                                    required: {value: true, errorMessage: "Nomini (uzbek) kiriting!"}
                                                }
                                            }/>

                                        </div>
                                        <div className="col-12">
                                            <AvField defaultValue={current.productTypeId ? current.nameRu : null}
                                                     label="Nomi (ru)"
                                                     name="nameRu" placeholder="Ruscha nomi" required validate={
                                                {
                                                    required: {value: true, errorMessage: "Nomini (ru) kiriting!"}
                                                }
                                            }/>

                                        </div>
                                        <div className="col-12">
                                            <AvField defaultValue={current.productTypeId ? current.nameEn : null}
                                                     label="Nomi (en)"
                                                     name="nameEn" placeholder="Inglizcha nomi" required validate={
                                                {
                                                    required: {value: true, errorMessage: "Nomini (en) kiriting!"}
                                                }
                                            }/>
                                        </div>
                                    </div>
                                </div>

                                <div className="mt-auto mb-2">
                                    <div className="d-flex justify-content-start">
                                        <div className="">
                                            <Button type='submit'
                                                    className="benefitBtn addBtnBenefit mr-3">Saqlash</Button>
                                            <Button className="benefitBtn cancelBtnBenefit"
                                                    onClick={() => this.props.value.toggle(null)}>Bekor qilish</Button>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </AvForm>
                    </ModalBody>
                </Modal>
            </div>
        );
    }
}

export default AddProductType;

