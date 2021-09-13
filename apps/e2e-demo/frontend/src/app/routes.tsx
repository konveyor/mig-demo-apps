import * as React from 'react';
import { Route, RouteComponentProps, Switch } from 'react-router-dom';
import { accessibleRouteChangeHandler } from '@app/utils/utils';
import { Customers } from '@app/Customers/Customers';
import { CustomerDetail } from '@app/Customers/CustomerDetail';
import { Orders } from '@app/Orders/Orders';
import { OrderDetail } from '@app/Orders/OrderDetail';
import { Products } from '@app/Products/Products';
import { ProductDetail } from '@app/Products/ProductDetail';
import { NotFound } from '@app/NotFound/NotFound';
import { useDocumentTitle } from '@app/utils/useDocumentTitle';
import { LastLocationProvider, useLastLocation } from 'react-router-last-location';

let routeFocusTimer: number;
export interface IAppRoute {
  label?: string; // Excluding the label will exclude the route from the nav sidebar in AppLayout
  /* eslint-disable @typescript-eslint/no-explicit-any */
  component: React.ComponentType<RouteComponentProps<any>> | React.ComponentType<any>;
  /* eslint-enable @typescript-eslint/no-explicit-any */
  exact?: boolean;
  path: string;
  title: string;
  isAsync?: boolean;
  routes?: undefined;
}

export interface IAppRouteGroup {
  label: string;
  routes: IAppRoute[];
}

export type AppRouteConfig = IAppRoute | IAppRouteGroup;

const routes: AppRouteConfig[] = [
  {
    component: Customers,
    exact: true,
    label: 'Customers',
    path: '/',
    title: 'React App | Customer resource list',
  },
  {
    component: CustomerDetail,
    exact: true,
    isAsync: true,
    path: '/customers/detail/:id',
    title: 'React App | Customer resource detail page',
  },
  {
    component: Orders,
    exact: true,
    label: 'Orders',
    path: '/orders',
    title: 'React App | Orders resource list',
  },
  {
    component: OrderDetail,
    exact: true,
    isAsync: true,
    path: '/orders/detail/:id',
    title: 'React App | Order resource detail page',
  },
  {
    component: Products,
    exact: true,
    label: 'Products',
    path: '/products',
    title: 'React App | Products resource list',
  },
  {
    component: ProductDetail,
    exact: true,
    isAsync: true,
    path: '/products/detail/:slug',
    title: 'React App | Products resource detail page',
  },
];

// a custom hook for sending focus to the primary content container
// after a view has loaded so that subsequent press of tab key
// sends focus directly to relevant content
const useA11yRouteChange = (isAsync: boolean) => {
  const lastNavigation = useLastLocation();
  React.useEffect(() => {
    if (!isAsync && lastNavigation !== null) {
      routeFocusTimer = accessibleRouteChangeHandler();
    }
    return () => {
      window.clearTimeout(routeFocusTimer);
    };
  }, [isAsync, lastNavigation]);
};

const RouteWithTitleUpdates = ({ component: Component, isAsync = false, title, ...rest }: IAppRoute) => {
  useA11yRouteChange(isAsync);
  useDocumentTitle(title);

  function routeWithTitle(routeProps: RouteComponentProps) {
    return <Component {...rest} {...routeProps} />;
  }

  return <Route render={routeWithTitle} {...rest}/>;
};

const PageNotFound = ({ title }: { title: string }) => {
  useDocumentTitle(title);
  return <Route component={NotFound} />;
};

const flattenedRoutes: IAppRoute[] = routes.reduce(
  (flattened, route) => [...flattened, ...(route.routes ? route.routes : [route])],
  [] as IAppRoute[]
);

const AppRoutes = (): React.ReactElement => (
  <LastLocationProvider>
    <Switch>
      {flattenedRoutes.map(({ path, exact, component, title, isAsync }, idx) => (
        <RouteWithTitleUpdates
          path={path}
          exact={exact}
          component={component}
          key={idx}
          title={title}
          isAsync={isAsync}
        />
      ))}
      <PageNotFound title="404 Page Not Found" />
    </Switch>
  </LastLocationProvider>
);

export { AppRoutes, routes };
