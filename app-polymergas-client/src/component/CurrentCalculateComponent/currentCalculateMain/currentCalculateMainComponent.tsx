import React from 'react';
import './index.less'
import {Button, Col, Modal, ModalBody, ModalHeader, Row} from "reactstrap";
import posed from "react-pose";
import CurrentCalculateLinksZero from "@/component/CurrentCalculateComponent/currentCalculateLinksZero";
import CurrentCalculateLinksOne from "@/component/CurrentCalculateComponent/currentCalculateLinksOne";


interface initialState {
  coating:number,
  level:number,
  currentCalculateLinks:number,
  open: number | boolean,
  answer: number| boolean ,
}

const Content = posed.div({
  closed: {height: 0},
  open: {height: 'auto'},
});
class CurrentCalculateLinksMain extends React.Component {
  state: initialState;
  props: any

  constructor(props: any) {
    super(props);
    this.state = {
      coating:1,
      level:1,
      currentCalculateLinks: 0,
      open: false,
      answer: false,
    }
  }
componentDidMount(): void {
    this.setState({open:this.props.open,level:this.props.level?this.props.level:1,coating:this.props.coating?this.props.coating:1})
}

  render() {
    const {open,level,coating} = this.state;
    const data = [
      {
        title: "",
      }
    ];
    const changeCurrentCalculateLinks = (currentCalculateLinks: any) => {
      this.setState({currentCalculateLinks})
    };
    let {currentCalculateLinks} = this.state;

const setAnswer=(val:number)=>{
  this.setState({answer:val})
}
    return (
      <div className="currentCalculateMainComponent">
        {data.map(({title}, i) => (
          <div>
            <div className="d-flex align-items-center h-100 w-auto">
              <div className="">
                <div className="change-order-btn btn-size-md "
                     onClick={() => this.setState({open: open === i ? false : i})}>
                  <button>
                    <span className="icon icon-calculator-line"/>
                  </button>
                </div>
              </div>
              <div className="ml-3"
                   onClick={() => this.setState({open: open === i ? false : i})}>
                <p className="mb-0 fs-12 font-family-semi-bold">
                  Qancha bo'yoq kerak?
                </p>
              </div>
            </div>
            {/*<Content className="content" pose={open === i ? 'open' : 'closed'}>*/}
            {/*  <div className="content-wrapper">*/}
            {/*    <div className="h-100">*/}
            {/*      <div className="text-center mb-3">*/}
            {/*        <p className="mb-0 fs-14 font-family-semi-bold">*/}
            {/*          Siz hududni bilasizmi?*/}
            {/*        </p>*/}
            {/*      </div>*/}
            {/*      <div className="reportMainSectionCol1 d-flex justify-content-center align-items-center h-100">*/}
            {/*        <div className={"d-flex reportMainSectionSub fs-16 font-family-medium"}>*/}
            {/*          <div onClick={() => changeCurrentCalculateLinks(0)}*/}
            {/*               className={currentCalculateLinks === 0 ? "mx-1 active" : "rateLink mx-1"}> Ha*/}
            {/*          </div>*/}
            {/*          <div onClick={() => changeCurrentCalculateLinks(1)}*/}
            {/*               className={currentCalculateLinks === 1 ? "mx-1 active" : "rateLink mx-1"}> Yoq*/}
            {/*          </div>*/}
            {/*        </div>*/}
            {/*      </div>*/}
            {/*    </div>*/}
            {/*    {console.log(this.state.answer)}*/}
            {/*    {this.state.answer?this.state.answer:*/}
            {/*      <Col md={12} className="reportMainSectionCol2">*/}
            {/*        <div className="adminLinksMainContent">*/}
            {/*          <div className="d-flex justify-content-center">*/}
            {/*            <div className="w-100">*/}
            {/*              {currentCalculateLinks === 0 ? <CurrentCalculateLinksZero level={level} coating={coating} setAnswer={setAnswer}*/}
            {/*                                                                        changeCurrentEditAndSettingsLinks={changeCurrentCalculateLinks}/> : ""}*/}
            {/*              {currentCalculateLinks === 1 ? <CurrentCalculateLinksOne level={level} coating={coating} setAnswer={setAnswer}*/}
            {/*                                                                       changeCurrentEditAndSettingsLinks={changeCurrentCalculateLinks}/> : ""}*/}
            {/*            </div>*/}
            {/*          </div>*/}
            {/*        </div>*/}
            {/*      </Col>*/}
            {/*    }*/}

            {/*  </div>*/}
            {/*</Content>*/}
          </div>
        ))}
      </div>
    );
  }
}

export default CurrentCalculateLinksMain;
