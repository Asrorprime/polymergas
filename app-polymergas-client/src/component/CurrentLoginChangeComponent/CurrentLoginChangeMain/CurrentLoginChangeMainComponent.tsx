import React from 'react';
import {Col} from "reactstrap";
import CurrentLoginChangeLinksZero from "@/component/CurrentLoginChangeComponent/CurrentLoginChangeLinksZero";
import CurrentLoginChangeLinksOne from "@/component/CurrentLoginChangeComponent/CurrentLoginChangeLinksOne";


interface initialState {
  currentLoginChangeLinks: number,
}

class CurrentLoginChangesLinksMain extends React.Component {
  state: initialState;
  props: any

  constructor(props: any) {
    super(props);
    this.state = {
      currentLoginChangeLinks: 0,
    }
  }

  render() {
    const changeCurrentLoginChangeLinks = (currentLoginChangeLinks: any) => {
      this.setState({currentLoginChangeLinks})
    };
    let {currentLoginChangeLinks} = this.state;


    return (
      <div className="currentLoginChangesMainComponent">
        <div className="">
          <div className="reportMainSectionCol1 d-flex justify-content-center pt-2">
            <div className={"d-flex reportMainSectionSub fs-16 font-family-medium"}>
              <div onClick={() => changeCurrentLoginChangeLinks(0)}
                   className={currentLoginChangeLinks === 0 ? "mx-1 active" : "rateLink mx-1"}> Parol
              </div>
              <div onClick={() => changeCurrentLoginChangeLinks(1)}
                   className={currentLoginChangeLinks === 1 ? "mx-1 active" : "rateLink mx-1"}> Login
              </div>
            </div>
          </div>
        </div>
        <Col md={12} className="reportMainSectionCol2">
          <div className="adminLinksMainContent">
            <div className="d-flex justify-content-center">
              <div className="w-100">
                {currentLoginChangeLinks === 0 ? <CurrentLoginChangeLinksZero
                  changeCurrentEditAndSettingsLinks={changeCurrentLoginChangeLinks}/> : ""}
                {currentLoginChangeLinks === 1 ? <CurrentLoginChangeLinksOne
                  changeCurrentEditAndSettingsLinks={changeCurrentLoginChangeLinks}/> : ""}
              </div>
            </div>
          </div>
        </Col>
      </div>
    );
  }
}

export default CurrentLoginChangesLinksMain;
