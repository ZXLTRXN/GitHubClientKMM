//
//  ColorManager.swift
//  GitHubClient
//
//  Created by Ilya Shevtsov on 05.07.2022.
//

import UIKit

class ColorManager {
    private let colors: [String: String]
    
    static let shared = ColorManager()
    
    init() {
        guard let asset = NSDataAsset(name: "github_colors") else {
            fatalError("Missing data asset: github_colors")
        }
        let data = asset.data
        colors = try! JSONDecoder().decode([String: String].self, from: data)
    }
    
    func getColor(language: String?) -> UIColor {
        let defaultLanguageColor: UIColor = UIColor(named: "DefaultLanguage")!
        guard let language = language,
              let hexColor = colors[language],
              let color = UIColor(hex: hexColor)
        else { return defaultLanguageColor }
        return color
    }
}
