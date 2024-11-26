import { Component, OnInit } from '@angular/core';
import { RouterModule, RouterOutlet } from '@angular/router';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule} from '@angular/material/icon';


@Component({
  selector: 'app-header',
  standalone: true,
  imports: [RouterOutlet, RouterModule, MatToolbarModule, MatButtonModule, MatIconModule],
  templateUrl: './header.component.html',
  styleUrl: './header.component.scss'
})
export class HeaderComponent implements OnInit  {
    
  title = 'SUN Item Analysis';
  isDark = false;

  THEME_SAVED = 'LOCAL_THEME';

  toggleTheme(){
    this.isDark = !this.isDark;
    document.body.classList.toggle('dark');
    document.body.classList.toggle('light');
    const theme  = this.isDark ? 'dark' : 'light';
    localStorage.setItem(this.THEME_SAVED, theme);
  }

  
  ngOnInit(): void {
    let savedTheme = localStorage.getItem(this.THEME_SAVED);
    if(!savedTheme){
      const prefersDarkScheme = window.matchMedia("(prefers-color-scheme: dark)");
      savedTheme = prefersDarkScheme.matches ? 'dark' : 'light';
      localStorage.setItem(this.THEME_SAVED, savedTheme);
    }
    if(savedTheme === 'dark'){
      this.toggleTheme();
    }
    
  }

  

}
