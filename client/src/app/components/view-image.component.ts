import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { FileUploadService } from '../fileupload.service';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-view-image',
  standalone: false,
  templateUrl: './view-image.component.html',
  styleUrl: './view-image.component.css'
})
export class ViewImageComponent implements OnInit, OnDestroy {
  postId: string = "";
  paramSub!: Subscription
  imageData: any

  constructor(private router:Router, private activatedRoute:ActivatedRoute, private fileUploadSvc:FileUploadService) {}

  ngOnInit(): void {
    this.paramSub = this.activatedRoute.params.subscribe(
      async(params) => {
        this.postId = params['postId']
        
        let result = await this.fileUploadSvc.getImage(this.postId)
        console.info('>>> result', result)
        
        this.imageData = result.image
    })
  }

  ngOnDestroy(): void {
    this.paramSub.unsubscribe()
  }
}
