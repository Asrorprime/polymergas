import React, {Component} from 'react';
interface  initialState{
}
class Header extends Component {
  state:initialState;
  constructor(props:any) {
    super(props);
    this.state={
      type:""
    }
  }
  render() {
    return (
      <div>
        Header
      </div>
    );
  }
}

export default Header;
