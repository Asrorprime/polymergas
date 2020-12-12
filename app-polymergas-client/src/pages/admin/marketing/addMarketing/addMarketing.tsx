import React, {Component} from 'react';
import {Button, Modal, ModalBody, ModalHeader} from 'reactstrap';
import {connect} from "@@/plugin-dva/exports";
// @ts-ignore
import makeAnimated from 'react-select/animated';
// @ts-ignore
import {AvField, AvForm} from 'availity-reactstrap-validation';
// @ts-ignore

const animatedComponents = makeAnimated();

interface initialState {
    file: any,
}

// @ts-ignore
@connect(({marketing, app}) => ({marketing, app}))
class AddMarketing extends Component {
    state: initialState;
    props: any;

    // onChange: any;

    constructor(props: any) {
        super(props);
        this.state = {
            file: []
        }
        // this.onChange = this.onChange.bind(this);
    }

    // @ts-ignore

    render() {
        const props = this.props;
        // @ts-ignore
        const {className, dispatch} = props;
        const {current} = props.value;
        const savePhoto = (e: any) => {
            dispatch({
                type: "app/uploadFile",
                payload: {
                    file: e.target.files,
                    fileUpload: true,
                    type: e.target.name
                }
            }).then((res: any) => {
                this.state.file[0] = res;
                this.setState(this.state);
            })
        };
        const saveMarketing = (a: any, v: any) => {
            v = {...v, photoId: this.state.file[0]}
            this.props.dispatch({
                type: "marketing/saveMarketing",
                payload: {
                    id: current.id ? current.id : null,
                    ...v
                }
            }).then((res: any) =>
                dispatch({
                    type: "marketing/getMarketing",
                    payload: {
                        page: 0,
                        size: 10
                    }
                }));
            this.props.value.toggle()
        };
        return (
            <div className='right-modal'>
                <Modal id="leftsite"
                       centered={true}
                       isOpen={props.value.isOpen}
                       className={className}>
                    <ModalHeader className='position-relative text-center'
                                 toggle={() => this.props.value.toggle(null)}>
                        {
                            !current.id ?
                                <p className="mb-0 fs-16 font-family-semi-bold"> Reklama qo’shish </p> :
                                <p className="mb-0 fs-16 font-family-semi-bold"> Aksiya o'zgartirish</p>
                        }
                    </ModalHeader>
                    <ModalBody>
                        <AvForm onValidSubmit={saveMarketing}
                                className="formModall">
                            <div className="d-flex flex-column h-100">
                                <div className="">

                                    <div className="row m-0 p-0 product-img">
                                        <div className="d-flex">
                                            {this.state.file ? this.state.file.map((img: string) =>
                                                <div className="">
                                                    <img
                                                        src={"/api/file/" + img}
                                                        alt="" className='product-img'/>
                                                </div>) : null}
                                            <div className="ml-3">
                                                <div className="d-flex align-items-center h-100">
                                                    <div className="uploadInput my-auto">
                                                        <label className="upload-area">
                                                            <AvField required={current.id ? false : true}
                                                                     id="uploadFile" name="photoId"
                                                                     type="file" onChange={savePhoto} validate={
                                                                {
                                                                    required: {
                                                                        value: true,
                                                                        errorMessage: "Rasmni kiriting!"
                                                                    }
                                                                }
                                                            }/>
                                                            <span className="upload-button">
                        <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" width="24" height="24"><path
                            fill="none" d="M0 0h24v24H0z"/><path
                            d="M21 15v3h3v2h-3v3h-2v-3h-3v-2h3v-3h2zm.008-12c.548 0 .992.445.992.993V13h-2V5H4v13.999L14 9l3 3v2.829l-3-3L6.827 19H14v2H2.992A.993.993 0 0 1 2 20.007V3.993A1 1 0 0 1 2.992 3h18.016zM8 7a2 2 0 1 1 0 4 2 2 0 0 1 0-4z"
                            fill="rgba(147,144,144,1)"/>
                        </svg>
                     </span>
                                                        </label>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                    <div className="col-12 my-2">
                                        <AvField label='Sarlavha (uzbek)'
                                                 defaultValue={current.id ? current.titleUz : null}
                                                 name="titleUz"
                                                 placeHolder='misol uchun 9-may uchun'
                                                 required
                                                 type='string'/>
                                    </div>
                                    <div
                                        className="col-12 my-2">
                                        <AvField label='Sarlavha (rus)'
                                                 defaultValue={current.id ? current.titleRu : null}
                                                 name="titleRu"
                                                 placeHolder='пример для 9-мая'
                                                 required
                                                 type='string'/>
                                    </div>
                                    <div
                                        className="col-12 my-2">
                                        <AvField label='Sarlavha (en)'
                                                 defaultValue={current.id ? current.titleRu : null}
                                                 name="titleEn"
                                                 placeHolder='example for 9-may'
                                                 required
                                                 type='string'/>
                                    </div>
                                    <div
                                        className="col-12 my-2">
                                        <AvField label='Izoh (uzbek)'
                                                 defaultValue={current.id ? current.textUz : null}
                                                 name="textUz"
                                                 placeHolder='пример для 9-мая'
                                                 required
                                                 type='textarea'/>
                                    </div>
                                    <div
                                        className="col-12 my-2">
                                        <AvField label='Izoh (rus)'
                                                 defaultValue={current.id ? current.textRu : null}
                                                 name="textRu"
                                                 placeHolder='пример для 9-мая'
                                                 required
                                                 type='textarea'/>
                                    </div>
                                    <div
                                        className="col-12 my-2">
                                        <AvField label='Izoh (en)'
                                                 defaultValue={current.id ? current.textEn : null}
                                                 name="textEn"
                                                 placeHolder='example for 9-may'
                                                 required
                                                 type='textarea'/>
                                    </div>
                                </div>
                                <div className="mt-auto mb-2">
                                    <div className="d-flex justify-content-start">
                                        <div className="">
                                            <Button type='submit'
                                                    className="benefitBtn addBtnBenefit mr-3">Saqlash </Button>
                                            <Button
                                                className="benefitBtn cancelBtnBenefit"
                                                onClick={() => this.props.value.toggle(null)}>Bekor qilish </Button>
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

export default AddMarketing;

