import {Interval} from "./interval";

export class StorageNode {
    public id: number;
    public capacity: number;
    public intervals: Interval[];

    public pendingCapacityChange: number;
}
