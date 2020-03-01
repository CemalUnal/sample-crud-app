import React, { Component } from 'react';
import './App.css';
import CustomerList from './Customer/CustomerList';
import logo from './logo.svg'

class App extends Component {
  render() {
    return (

      <div className="App">
        <header className="App-header">
          <img src={logo} className="App-logo" alt="logo" />
        </header>
        <CustomerList/>
      </div>
    );
  }
}

export default App;
