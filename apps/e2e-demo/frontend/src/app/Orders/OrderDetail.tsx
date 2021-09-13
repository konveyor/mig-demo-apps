import * as React from 'react';
import {
  PageSection,
  Card,
  CardBody,
  Grid,
  GridItem,
  Title,
  DescriptionList,
  DescriptionListTerm,
  DescriptionListGroup,
  DescriptionListDescription,
  DataList,
  DataListItem,
  DataListItemRow,
  DataListItemCells,
  DataListCell
} from '@patternfly/react-core';
import { InfoAltIcon } from '@patternfly/react-icons';

export interface IOrderDetailProps {
  sampleProp?: string;
}

const OrderDetail: React.FunctionComponent<IOrderDetailProps> = (props) => {

  const [orderData, setOrderData] = React.useState<any>();

  // eslint-disable-next-line @typescript-eslint/ban-ts-comment
  // @ts-ignore
  const getOrderData = () => fetch(`/orders-api/orders/${props.computedMatch.params.id}`);

  React.useEffect(() => {
    getOrderData().then((result) => result.json()).then(data => {
      setOrderData(data)
    });
  }, []);

  return (
    <PageSection>

      {orderData && (<>
        <Grid hasGutter>
          <GridItem span={2} smRowSpan={12}>
            <Title className="pf-u-mb-sm" headingLevel="h1">Customer details</Title>
            <Card isFlat>
              <Grid>
                <GridItem>
                  <CardBody>
                    <DescriptionList>
                      <DescriptionListGroup>
                        <DescriptionListTerm>Customer name</DescriptionListTerm>
                        <DescriptionListDescription>{orderData.customer.name} {orderData.customer.surname}</DescriptionListDescription>
                      </DescriptionListGroup>
                      <DescriptionListGroup>
                        <DescriptionListTerm>Customer Address</DescriptionListTerm>
                        <DescriptionListDescription>{orderData.customer.address}</DescriptionListDescription>
                      </DescriptionListGroup>
                      <DescriptionListGroup>
                        <DescriptionListTerm>Customer City</DescriptionListTerm>
                        <DescriptionListDescription>{orderData.customer.city}</DescriptionListDescription>
                      </DescriptionListGroup>
                      <DescriptionListGroup>
                        <DescriptionListTerm>Customer Zip code</DescriptionListTerm>
                        <DescriptionListDescription>{orderData.customer.zipCode}</DescriptionListDescription>
                      </DescriptionListGroup>
                    </DescriptionList>
                  </CardBody>
                </GridItem>
              </Grid>
            </Card>
          </GridItem>
          <GridItem span={10} smRowSpan={12}>
            {orderData && (<>
              <Title className="pf-u-mb-sm" headingLevel="h2">Order details</Title>
              <DataList aria-label="Simple data list example">
                {orderData.items.map((data, idx) => {
                  return (
                    <DataListItem key={idx} aria-labelledby={`item-name-${idx}`}>
                      <DataListItemRow>
                        <DataListItemCells
                          dataListCells={[
                            <DataListCell key={`item-name-${data.id}`}>
                              <span id={`item-name-${data.id}`}>Product {data.product.name}</span>
                            </DataListCell>,
                            <DataListCell key={`item-id-${data.id}`}>ID {data.product.id}</DataListCell>,
                            <DataListCell key={`item-description-${orderData.id}`}>
                              Description: {data.product.description}
                            </DataListCell>,
                            <DataListCell key={`item-price-${orderData.id}`}>Price {data.price}</DataListCell>,
                            <DataListCell key={`item-quantity-${orderData.id}`}>Quantity {data.quantity}</DataListCell>,
                          ]}
                        />
                      </DataListItemRow>
                    </DataListItem>
                  )
                })}
              </DataList>
            </>
            )}
          </GridItem>
        </Grid>
      </>
      )}

    </PageSection>
  )
}

export { OrderDetail };
