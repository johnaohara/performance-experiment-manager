import * as React from 'react';
import '@patternfly/react-core/dist/styles/base.css';
import { BrowserRouter as Router } from 'react-router-dom';
import { AppLayout } from './AppLayout/AppLayout';
import { AppRoutes } from './routes';
import './index.css';
// @ts-ignore
import ReactDOM from 'react-dom';
// import App from './App';
import reportWebVitals from './reportWebVitals';

const App: React.FunctionComponent = () => (
    <Router>
        <AppLayout>
            <AppRoutes />
        </AppLayout>
    </Router>
);

ReactDOM.render(<App />, document.getElementById("root") as HTMLElement);

export default App;

// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals();
