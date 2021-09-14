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
import { InfoAltIcon, UserAltIcon, ArrowLeftIcon, ArrowRightIcon } from '@patternfly/react-icons';

const Customers: React.FunctionComponent = () => {
  const [hasData, setHasData] = React.useState<any>([]);
  const [curPage, setCurPage] = React.useState(0);

  const history = useHistory();

  // https://gateway.fedev.10.19.2.21.nip.io:32013/customers?page=0&size=3&sort=name

  const getCustomers = () => fetch(`/customers-api/customers?page=${curPage}`, {
    // method: 'GET', // or 'PUT'
    // headers: {
    //   'Content-Type': 'application/json',
    // },
    // body: JSON.stringify(data),
  });

  React.useEffect(() => {

    getCustomers()
      .then(response => response.json())
      .then(data => {
        setHasData(data);
      })
      .catch((error) => {
        console.error('Error:', error);
      });

  }, [curPage]);

  return (
    <PageSection>
      <TextContent>
        <Title headingLevel="h1" className="pf-u-mb-sm">Customer resource list (Page {curPage + 1} of {hasData.totalPages})</Title>
      </TextContent>

      {hasData.content && hasData.content !== [] && <DataList aria-label="Simple data list example">
        {hasData.content.map((data, idx) => {
          return (
            <DataListItem key={idx} aria-labelledby={`item-name-${idx}`}>
              <DataListItemRow>
                <DataListItemCells
                  dataListCells={[
                    <DataListCell key={`item-name-${data.id}`}>
                      <span id={`item-name-${data.id}`}>{data.name} {data.surname}</span>
                    </DataListCell>,
                    <DataListCell key={`item-id-${data.id}`}>{data.id}</DataListCell>,
                    <DataListCell key={`item-address-${data.id}`}>{data.address}</DataListCell>,
                    <DataListCell key={`item-city-${data.id}`}>{data.city}</DataListCell>,
                    <DataListCell key={`item-country-${data.id}`}>{data.country}</DataListCell>,
                  ]}
                />
                <DataListAction
                  aria-labelledby="single-action-item1 single-action-action1"
                  id="single-action-action1"
                  aria-label="Actions"
                >
                  <Button onClick={() => {
                    history.push(`/customers/detail/${data.id}`);
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

export { Customers };
