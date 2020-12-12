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
  productId: any
}

// @ts-ignore
@connect(({sale, app}) => ({sale, app}))
class AddSale extends Component {
  state: initialState;
  props: any;

  // onChange: any;

  constructor(props: any) {
    super(props);
    this.state = {
      minDate: '',
      currentProductMeasure: null,
      maxDate: null,
    }
    // this.onChange = this.onChange.bind(this);
  }

  // @ts-ignore

  render() {
    const props = this.props;
    // @ts-ignore
    const {className, dispatch} = props;
    const {current} = props.value;
    const saveSale = (a: any, v: any) => {
      v = {...v, active: true}
      v.startDate = new Date(v.startDate);
      v.endDate = new Date(v.endDate);
      this.props.dispatch({
        type: "sale/saveSale",
        payload: {
          id: current.id ? current.id : null,
          ...v
        }
      }).then((res: any) =>
        dispatch({
          type: "sale/getSale",
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
                <p className="mb-0 fs-16 font-family-semi-bold"> Aksiya qo’shish </p> :
                <p className="mb-0 fs-16 font-family-semi-bold"> Aksiya o'zgartirish</p>
            }
          </ModalHeader>
          <ModalBody>
            <AvForm onValidSubmit={saveSale}
                    className="formModall">
              <div className="d-flex flex-column h-100">
                <div className="">
                  <div className="row mt-3">
                    <div className="col-12 mt-2">
                      <AvField label='Mavzu (uzbek)'
                               defaultValue={current.id ? current.titleUz : null}
                               name="titleUz"
                               placeHolder='misol uchun 9-may uchun'
                               required
                               type='string'/>
                    </div>
                    <div
                      className="col-12 mt-2">
                      <AvField label='Mavzu (rus)'
                               defaultValue={current.id ? current.titleRu : null}
                               name="titleRu"
                               placeHolder='пример для 9-мая'
                               required
                               type='string'/>
                    </div>
                    <div
                      className="col-12 mt-2">
                      <AvField label='Mavzu (eng)'
                               defaultValue={current.id ? current.titleEn : null}
                               name="titleEn"
                               placeHolder='example for 9 may'
                               required
                               type='string'/>
                    </div>
                    <div
                      className="col-12 mt-2">
                      <AvField label='Izoh(uz)'
                               defaultValue={current.id ? current.descriptionUz : null}
                               name="descriptionUz"
                               placeHolder='aksiya haqida izoh'
                               required
                               type='textarea'/>
                    </div>
                    <div
                      className="col-12 mt-2">
                      <AvField label='Izoh(ru)'
                               defaultValue={current.id ? current.descriptionRu : null}
                               name="descriptionRu"
                               placeHolder='комментарий по скидке'
                               required
                               type='textarea'/>
                    </div>
                    <div
                      className="col-12 mt-2">
                      <AvField label='Izoh(en)'
                               defaultValue={current.id ? current.descriptionEn : null}
                               name="descriptionEn"
                               placeHolder='comment on the sale'
                               required
                               type='textarea'/>
                    </div>
                    <div
                      className="col-12 my-2">
                      <AvField label='dan'
                               max={current.id ? current.endDate.substring(0, 10) : this.state.maxDate}
                               onChange={(a, v) => this.setState({minDate: v})}
                               defaultValue={current.id ? current.startDate.substring(0, 10) : null}
                               name="startDate"
                               placeHolder='...dan'
                               required
                               type='date'/>
                    </div>
                    <div className="col-12 my-2">
                      <AvField label='...gacha'
                               defaultValue={current.id ? current.endDate.substring(0, 10) : null}
                               name="endDate"
                               min={current.id ? current.startDate.substring(0, 10) : this.state.minDate}
                               onChange={(a, v) => this.setState({maxDate: v})}
                               placeHolder='...gacha'
                               required
                               type='date'/>
                    </div>
                  </div>

                  <div className="col-12 my-2">
                    <AvField label='Har bir kg mahsulot uchun necha ball berilishi'
                             defaultValue={current.id ? current.ball : null}
                             name="ball"
                             placeHolder='0.03'
                             required
                             type='string'/>
                  </div>
                </div>
                <div className="mt-auto mb-2">
                  <div className="d-flex justify-content-start">
                    <div className="">
                      <Button type='submit'
                              className="benefitBtn addBtnBenefit mr-3"> Saqlash </Button>
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

export default AddSale;

