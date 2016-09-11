import {Component, ViewChild, ElementRef} from '@angular/core';

@Component({
    selector: 'interval',
    template: `hi2 <canvas #canvas [width]="width" [height]="height"></canvas>`
})
export class IntervalComponent {
    @ViewChild("canvas") canvas: ElementRef;

    private width: number = 200;
    private height: number = 30;

    ngAfterViewInit() {
        let nativeCanvas: HTMLCanvasElement = this.canvas.nativeElement;
        let context: CanvasRenderingContext2D = nativeCanvas.getContext('2d');

        context.beginPath();
        context.lineWidth = 4;

        var halfLineWidth = context.lineWidth / 2;
        context.moveTo(halfLineWidth, 0);
        context.lineTo(halfLineWidth, this.height);

        context.moveTo(halfLineWidth, this.height / 2);
        context.lineTo(nativeCanvas.width - halfLineWidth, this.height / 2);

        context.moveTo(nativeCanvas.width - halfLineWidth, 0);
        context.lineTo(nativeCanvas.width - halfLineWidth, this.height);
        context.stroke();
    }
}