import {Component, ViewChild, ElementRef, Input} from '@angular/core';
import {Interval} from "./entities/interval";

@Component({
    selector: 'interval',
    template: `
    <canvas #canvas width="200" height="30"></canvas>
`
})
export class IntervalComponent {
    @ViewChild("canvas") canvas: ElementRef;

    @Input() public interval: Interval;

    ngAfterViewInit() {
        let nativeCanvas: HTMLCanvasElement = this.canvas.nativeElement;
        let context: CanvasRenderingContext2D = nativeCanvas.getContext('2d');

        context.beginPath();
        context.lineWidth = 4;

        var halfLineWidth = context.lineWidth / 2;
        if (this.interval.start > 0) {
            context.moveTo(this.interval.start * nativeCanvas.width + halfLineWidth, 0);
            context.lineTo(this.interval.start * nativeCanvas.width + halfLineWidth, nativeCanvas.height);
        }

        context.moveTo(this.interval.start * nativeCanvas.width + halfLineWidth, nativeCanvas.height / 2);
        context.lineTo(this.interval.end * nativeCanvas.width - halfLineWidth, nativeCanvas.height / 2);

        if (this.interval.end < 1) {
            context.moveTo(this.interval.end * nativeCanvas.width - halfLineWidth, 0);
            context.lineTo(this.interval.end * nativeCanvas.width - halfLineWidth, nativeCanvas.height);
        }
        context.stroke();
    }
}