import {Interval} from "./interval";
import {Data} from "./data";

export class StorageNode {
    public id: number;
    public capacity: number;
    public intervals: Interval[];
    public storedData: Data[];
}
