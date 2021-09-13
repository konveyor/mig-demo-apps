import * as React from 'react';
import {
  Button,
  PageSection,
  Title,
  TextContent,
  DataList,
  DataListItem,
  DataListItemRow,
  DataListItemCells,
  DataListCell,
  DataListAction
} from '@patternfly/react-core';
import { useHistory } from 'react-router-dom';
import { InfoAltIcon } from '@patternfly/react-icons';
import '@app/Orders/Orders.css';

const Orders: React.FunctionComponent = () => {

  const [hasData, setHasData] = React.useState<any>();
  const [curPage, setCurPage] = React.useState(0);

  const history = useHistory();

  const getOrders = () => fetch(`/orders-api/orders?page=${curPage}`);

  React.useEffect(() => {
    getOrders()
      .then((result) => result.json())
      .then(data => {
        setHasData(data);
      })
      .catch(error => {
        console.log('Error', error);
      })
  }, [curPage]);

  return (
    <PageSection>
      {hasData && (
        <TextContent>
          <Title headingLevel="h1" className="pf-u-mb-sm">Orders resource list (Page {curPage + 1} of {hasData.totalPages})</Title>
        </TextContent>
      )}

      {hasData && <DataList aria-label="Simple data list example">
        {hasData.content.map((data, idx) => {
          return (
            <DataListItem key={idx} aria-labelledby={`item-name-${idx}`}>
              <DataListItemRow>
                <DataListItemCells
                  dataListCells={[
                    <DataListCell key={`item-name-${data.id}`}>
                      <span id={`item-name-${data.id}`}>{data.customer.name} {data.customer.surname}</span>
                    </DataListCell>,
                    <DataListCell key={`item-id-${data.id}`}>{data.id}</DataListCell>,
                    <DataListCell key={`item-date-${data.id}`}>{data.date}</DataListCell>,
                    <DataListCell key={`item-address-${data.id}`}>{data.customer.address}</DataListCell>,
                    <DataListCell key={`item-city-${data.id}`}>{data.customer.city}</DataListCell>,
                    <DataListCell key={`item-country-${data.id}`}>{data.customer.country}</DataListCell>,
                  ]}
                />
                <DataListAction
                  aria-labelledby="single-action-item1 single-action-action1"
                  id="single-action-action1"
                  aria-label="Actions"
                >
                  <Button onClick={() => {
                    history.push(`/orders/detail/${data.id}`);
                  }} variant="link" icon={<InfoAltIcon />}>
                    See details
                  </Button>
                </DataListAction>
              </DataListItemRow>
            </DataListItem>
          )
        })}
      </DataList>
      }

    </PageSection>
  )
}

export { Orders };
