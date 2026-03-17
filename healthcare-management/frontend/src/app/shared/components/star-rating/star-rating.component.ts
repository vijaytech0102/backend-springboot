import { Component, Input, Output, EventEmitter } from '@angular/core';

@Component({
  selector: 'app-star-rating',
  templateUrl: './star-rating.component.html',
  styleUrls: ['./star-rating.component.scss']
})
export class StarRatingComponent {
  @Input() value: number = 0;
  @Input() readonly: boolean = false;
  @Output() ratingChanged = new EventEmitter<number>();

  stars: number[] = [1, 2, 3, 4, 5];

  onStarClick(rating: number): void {
    if (!this.readonly) {
      this.value = rating;
      this.ratingChanged.emit(rating);
    }
  }
}
