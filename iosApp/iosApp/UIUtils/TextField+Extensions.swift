//
//  TextFieldExtensions.swift
//  GitHubClient
//
//  Created by Ilya Shevtsov on 22.06.2022.
//

import UIKit

extension UITextField {
    func setBorderColor(_ color: CGColor, _ borderRadius: CGFloat = 8) {
        self.layer.borderWidth = 1.0
        self.layer.cornerRadius = borderRadius
        self.layer.borderColor = color
    }
}
