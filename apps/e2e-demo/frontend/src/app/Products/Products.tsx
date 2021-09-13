import * as React from 'react';
import {
  Button,
  PageSection,
  Card,
  CardBody,
  CardTitle,
  Gallery,
  GalleryItem,
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
import { InfoAltIcon, ArrowLeftIcon, ArrowRightIcon } from '@patternfly/react-icons';

const Products: React.FunctionComponent = () => {
  const [productsData, setProductsData] = React.useState<any>();
  const [curPage, setCurPage] = React.useState(0);

  const history = useHistory();

  const getProducts = () => fetch(`/products-api/products?page=${curPage}`);

  React.useEffect(() => {
    getProducts()
      .then(result => result.json())
      .then(data => {
        console.log('product data', data)
        setProductsData(data);
      });

  }, [curPage]);

  return (
    <PageSection>

      {productsData && (
        <TextContent>
          <Title headingLevel="h1" className="pf-u-mb-sm">Products resource list (Page {curPage + 1} of {productsData.totalPages})</Title>
        </TextContent>
      )}

      {productsData && <DataList aria-label="Simple data list example">
        {productsData.content.map((data, idx) => {
          return (
            <DataListItem key={idx} aria-labelledby={`item-name-${idx}`}>
              <DataListItemRow>
                <DataListItemCells
                  dataListCells={[
                    <DataListCell key={`item-name-${data.id}`}>
                      <span id={`item-name-${data.id}`}>{data.name}</span>
                    </DataListCell>,
                    <DataListCell key={`item-id-${data.id}`}>{data.id}</DataListCell>,
                    <DataListCell key={`item-description-${data.id}`}>{data.description}</DataListCell>
                  ]}
                />
                <DataListAction
                  aria-labelledby="single-action-item1 single-action-action1"
                  id="single-action-action1"
                  aria-label="Actions"
                >
                  <Button onClick={() => {
                    history.push(`/products/detail/${data.id}`);
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

export { Products };
