//
//  File.swift
//  GitHubClient
//
//  Created by Ilya Shevtsov on 28.06.2022.
//

import Foundation
import MaterialComponents.MaterialActivityIndicator
import UIKit

extension MDCActivityIndicator {
    func show() {
        self.isHidden = false
        self.startAnimating()
    }
    
    func hide() {
        self.isHidden = true
        self.stopAnimating()
    }
    
    func setColor(_ color: UIColor = .white) {
        self.cycleColors = [color]
    }
}
