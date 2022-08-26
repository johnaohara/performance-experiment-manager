import React, {useEffect, useRef} from 'react';
import {
    Progress,
    ProgressSize,
    ProgressMeasureLocation,
    ActionList,
    ActionListItem,
    Button,
    Dropdown,
    DropdownItem,
    DropdownSeparator,
    KebabToggle, Bullseye, EmptyState, EmptyStateVariant, EmptyStateIcon, EmptyStateBody, Title, ProgressVariant, Modal
} from '@patternfly/react-core';
import {
    TableComposable,
    Thead,
    Tr,
    Th,
    Tbody,
    Td
} from '@patternfly/react-table';

import {Link, Route} from "react-router-dom";
import {SearchIcon} from "@patternfly/react-icons";


type ExampleType = 'default' | 'compact' | 'compactBorderless';


interface IPipelineState {
    name: string;
    state: string;
}

interface IPipeline {
    pipelineName: string;
    total_Trials: number;
    currentTrial: number;
    currentState: string;
}

function useTraceUpdate(props) {
    const prev = useRef(props);
    useEffect(() => {
        const changedProps = Object.entries(props).reduce((ps, [k, v]) => {
            if (prev.current[k] !== v) {
                ps[k] = [prev.current[k], v];
            }
            return ps;
        }, {});
        if (Object.keys(changedProps).length > 0) {
            console.log('Changed props:', changedProps);
        }
        prev.current = props;
    });
}


export const PipelinesTable = () => {

    const [pipelines, setPipelines] = React.useState<IPipeline[]>([]);

    const columnNames = {
        name: 'Name',
        trial: 'Progress',
        actions: 'Actions'
    };

    let newExpRequest = new Request(
        "/api/pipeline",
        {
            method: "get",
        }
    )

    const updatePipelines = () => {
        fetch(newExpRequest)
            .then(res => res.json())
            .then(res => {
                setPipelines(res);
            })
    }
    useEffect(() => {
        updatePipelines();
        const eventSource = new EventSource("/api/pipeline/stream");
        eventSource.onmessage = (e) => {
            setPipelines(JSON.parse(e.data))
        };
        return () => {
            eventSource.close();
        };

    }, [])

    useTraceUpdate(pipelines);


    const ActionListSingleGroup = ({pipelineName}) => {
        const [isOpen, setIsOpen] = React.useState(false);

        const onToggle = (
            isOpen: boolean,
            event: MouseEvent | TouchEvent | KeyboardEvent | React.KeyboardEvent<any> | React.MouseEvent<HTMLButtonElement>
        ) => {
            event.stopPropagation();
            setIsOpen(isOpen);
        };

        const onSelect = (event: React.SyntheticEvent<HTMLDivElement, Event>) => {
            event.stopPropagation();
            setIsOpen(!isOpen);
        };


        const startRun = async () => {
            // alert ('start: ' + pipelineName)
            let state = '{"name": "' + pipelineName + '", "state": "RUNNING"};'

            let uppdateExpRequest = new Request(
                "/api/pipeline/state",
                {
                    method: "put",
                    headers: {'Content-Type': 'application/json'},
                    body: state
                }
            )

            let response = await fetch(uppdateExpRequest);

            if (response.ok) {
                let data = response.json();
                // console.log(data);
            } else {
                let data = await response.json();
            }

        }

        const deletePipeline = async () => {
            if( window.confirm('Are you sure you want to delete pipeline: ' + pipelineName + "?") ){
                let uppdateExpRequest = new Request(
                    "/api/pipeline/" + pipelineName,
                    {
                        method: "delete"
                    }
                )

                let response = await fetch(uppdateExpRequest);

                if (response.ok) {
                    updatePipelines();
                } else {
                    let data = await response.json();
                }
            }

        }


        const pauseRun = async () => {
            // alert ('start: ' + pipelineName)
            let state = '{"name": "' + pipelineName + '", "state": "PAUSED"};'

            let uppdateExpRequest = new Request(
                "/api/pipeline/state",
                {
                    method: "put",
                    headers: {'Content-Type': 'application/json'},
                    body: state
                }
            )

            let response = await fetch(uppdateExpRequest);

            if (response.ok) {
                let data = response.json();
                updatePipelines();
                // console.log(data);
            } else {
                let data = await response.json();
            }

        }

        const dropdownItems = [
            <DropdownItem key="run action" component="button" onClick={startRun}>
                Run
            </DropdownItem>,
            <DropdownItem key="pause action" component="button" onClick={pauseRun}>
                Pause
            </DropdownItem>,
            <DropdownSeparator key="separator"/>,
            <DropdownItem key="separated link">
                <Link to={'/pipeline/' + pipelineName}>Details</Link>
            </DropdownItem>,
            <DropdownSeparator key="separator"/>,
            <DropdownItem key="cancel action" component="button" onClick={deletePipeline}>
                Delete
            </DropdownItem>,
            // <DropdownItem key="separated link">Edit</DropdownItem>
        ];

        return (
            <React.Fragment>
                <ActionList>
                    <ActionListItem>
                        <Dropdown
                            // onSelect={onSelect}
                            toggle={<KebabToggle onToggle={onToggle}/>}
                            isOpen={isOpen}
                            isPlain
                            dropdownItems={dropdownItems}
                            position="right"
                        />
                    </ActionListItem>
                </ActionList>
            </React.Fragment>
        );
    };


    return (
        <TableComposable
            // aria-label="Simple table"
            variant='compact'
            borders={true}
            aria-label="Empty state table"
        >
            <Thead>
                <Tr>
                    <Th width={20}>{columnNames.name}</Th>
                    <Th width={70}>{columnNames.trial}</Th>
                    <Th width={10}>{columnNames.actions}</Th>
                </Tr>
            </Thead>
            <Tbody>
                <Tr hidden={!(pipelines.length === 0)}>
                    <Td colSpan={8}>
                        <Bullseye>
                            <EmptyState variant={EmptyStateVariant.small}>
                                <EmptyStateIcon icon={SearchIcon}/>
                                <Title headingLevel="h2" size="lg">
                                    No pipelines found
                                </Title>
                                <EmptyStateBody>Please create a new Pipeline and try again</EmptyStateBody>
                            </EmptyState>
                        </Bullseye>
                    </Td>
                </Tr>
                {pipelines.map(pipeline => (
                    <Tr key={pipeline.pipelineName}>
                        <Td dataLabel={columnNames.name}><Link
                            to={'/pipeline/' + pipeline.pipelineName}>{pipeline.pipelineName}</Link></Td>
                        <Td dataLabel={columnNames.trial}>
                            <Progress
                                measureLocation={ProgressMeasureLocation.outside}
                                value={pipeline.currentTrial / pipeline.total_Trials * 100.0}
                                size={ProgressSize.md}
                                variant={pipeline.currentState === 'RUNNING' ? ProgressVariant.success : ProgressVariant.warning}

                            /></Td>
                        <Td dataLabel={columnNames.actions}
                            isActionCell={true}><ActionListSingleGroup pipelineName={pipeline.pipelineName}/></Td>
                    </Tr>
                ))}
            </Tbody>
        </TableComposable>
    );
}


